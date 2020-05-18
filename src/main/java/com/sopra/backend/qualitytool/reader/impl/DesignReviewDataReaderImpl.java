package com.sopra.backend.qualitytool.reader.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sopra.backend.qualitytool.constants.ApplicationConstants;
import com.sopra.backend.qualitytool.model.source.DesignReviewSourceFileDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.reader.DesignReviewDataReader;

@Service
public class DesignReviewDataReaderImpl implements DesignReviewDataReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesignReviewDataReaderImpl.class);

	private static final int NO_COLUMNS = 5;

	/**
	 * Read and verify the contents of the designReviewFile if it is ok to be
	 * processed further
	 * 
	 * @param file
	 * @param designReviewSourceFile
	 */
	@Override
	public XSSFSheet readDesignReviewFile(String file, DesignReviewSourceFileDto designReviewSourceFile) {
		XSSFSheet spreadsheet = null;
		try (FileInputStream fis = new FileInputStream(new File(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fis);) {
			spreadsheet = workbook.getSheet(ApplicationConstants.FILE_SHEET);
			/* verifying column indices to process the file ahead */
			Boolean flagColumnIndex = verifyColumnIndices(spreadsheet, designReviewSourceFile);
			if (!flagColumnIndex) {
				return null;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return spreadsheet;
	}

	/**
	 * Check if the Design Review file has all the columns required to process
	 * the file .
	 * 
	 * @param spreadsheet
	 * @param designReviewSourceFileDto
	 * @return flagFound
	 */
	private boolean verifyColumnIndices(XSSFSheet spreadsheet, DesignReviewSourceFileDto designReviewSourceFileDto) {
		Boolean flagFound = false;
		Integer countColumnIndex = 0;

		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.matches(ApplicationConstants.RELEASE_REGEX)) {
						designReviewSourceFileDto.setPackColumnIndex(cell.getColumnIndex());
						designReviewSourceFileDto.setSourceFileFirstRow(cell.getRowIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.WORK_PRODUCT)) {
						designReviewSourceFileDto.setGroupColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.matches((ApplicationConstants.WORK_PRODUCT_REGEX))) {
						designReviewSourceFileDto.setBranchColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.REVIEW_TYPE_REGEX)) {
						designReviewSourceFileDto.setDefectColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.CLASS_OF_BUGS)) {
						designReviewSourceFileDto.setClassOfBugColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					}
				}
			}
		}
		if (countColumnIndex == NO_COLUMNS) {
			flagFound = true;
		}
		return flagFound;
	}

	/**
	 * Process Test Plan File Data to be added to the Quality File
	 * 
	 * @param sheet
	 * @param designReviewSourceFileDto
	 * @param listQualityDataDto
	 * 
	 */
	@Override
	public void filteringData(XSSFSheet sheet, DesignReviewSourceFileDto designReviewSourceFileDto,
			List<QualityDataDto> listQualityDataDto) {
		if (Objects.nonNull(designReviewSourceFileDto)) {
			int blocker = 0;
			int major = 0;
			int minor = 0;
			Integer startingRow = designReviewSourceFileDto.getSourceFileFirstRow();
			Integer branchColumnIndex = designReviewSourceFileDto.getBranchColumnIndex();
			String branchName = sheet.getRow(startingRow + 1).getCell(branchColumnIndex).getStringCellValue();
			Boolean lastRow = Boolean.FALSE;
			for (int rowIndex = startingRow + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (Objects.nonNull(row) && Objects.nonNull(row.getCell(branchColumnIndex))
						&& Objects.nonNull(row.getCell(branchColumnIndex).getStringCellValue())
						&& !(row.getCell(branchColumnIndex).getStringCellValue().trim().isEmpty())) {
					String currentBranchName = row.getCell(branchColumnIndex).getStringCellValue().trim();
					Integer rowNum = row.getRowNum();
					if ((rowNum == sheet.getLastRowNum()
							|| Objects.isNull(sheet.getRow(rowNum + 1).getCell(branchColumnIndex))
							|| sheet.getRow(rowNum + 1).getCell(branchColumnIndex).getStringCellValue().trim()
									.isEmpty())) {
						lastRow = true;
					}

					if (rowNum == startingRow + 1 || currentBranchName.equalsIgnoreCase(branchName)) {
						Cell cell = row.getCell(designReviewSourceFileDto.getDefectColumnIndex());
						if (Objects.nonNull(cell) && cell.getStringCellValue().equalsIgnoreCase("D")) {
							Cell classOfBugCell = row.getCell(designReviewSourceFileDto.getClassOfBugColumnIndex());
							switch ((int) classOfBugCell.getNumericCellValue()) {
							case 1:
								blocker++;
								break;
							case 2:
								major++;
								break;
							case 3:
								minor++;
								break;
							}
						}
						if (lastRow) {
							String packName = row.getCell(designReviewSourceFileDto.getPackColumnIndex())
									.getStringCellValue().trim();
							String groupName = row.getCell(designReviewSourceFileDto.getGroupColumnIndex())
									.getStringCellValue().trim();
							addDtoToList(listQualityDataDto, branchName, packName, groupName, blocker, major, minor);
						}

					} else if (!(currentBranchName.equalsIgnoreCase(branchName))) {
						// adding previous gcu to list of Dto
						String packName = sheet.getRow(rowNum - 1)
								.getCell(designReviewSourceFileDto.getPackColumnIndex()).getStringCellValue().trim();
						String groupName = sheet.getRow(rowNum - 1)
								.getCell(designReviewSourceFileDto.getGroupColumnIndex()).getStringCellValue().trim();
						addDtoToList(listQualityDataDto, branchName, packName, groupName, blocker, major, minor);
						major = blocker = minor = 0;
						branchName = currentBranchName;
						Cell cell = row.getCell(designReviewSourceFileDto.getDefectColumnIndex());
						if (Objects.nonNull(cell) && cell.getStringCellValue().equalsIgnoreCase("D")) {
							Cell classOfBugCell = row.getCell(designReviewSourceFileDto.getClassOfBugColumnIndex());
							switch ((int) classOfBugCell.getNumericCellValue()) {
							case 1:
								blocker++;
								break;
							case 2:
								major++;
								break;
							case 3:
								minor++;
								break;
							}
						}
						if (lastRow) {
							packName = row.getCell(designReviewSourceFileDto.getPackColumnIndex()).getStringCellValue()
									.trim();
							groupName = row.getCell(designReviewSourceFileDto.getGroupColumnIndex())
									.getStringCellValue().trim();
							addDtoToList(listQualityDataDto, branchName, packName, groupName, blocker, major, minor);
						}
					}
				}
			}
		}
	}

	/**
	 * Add the current values corresponding to the GCU in listQualityData
	 * 
	 * @param listQualityData
	 * @param branchName
	 * @param packName
	 * @param blocker
	 * @param major
	 * @param minor
	 */
	private void addDtoToList(List<QualityDataDto> listQualityData, String branchName, String packName,
			String groupName, Integer blocker, Integer major, Integer minor) {
		QualityDataDto qualityData = new QualityDataDto();
		qualityData.setBlocker(blocker);
		qualityData.setGroup(groupName);
		qualityData.setMajor(major);
		qualityData.setMinor(minor);
		if (branchName.contains(ApplicationConstants.DESIGN_REGEX)) {
			qualityData.setPhase(ApplicationConstants.DESIGN_PHASE);
		} else {
			qualityData.setPhase(ApplicationConstants.CODE_PHASE);
		}
		qualityData.setPack(ApplicationConstants.PACK_NAME_REGEX + packName.charAt(packName.length() - 1));
		listQualityData.add(qualityData);
	}

}
