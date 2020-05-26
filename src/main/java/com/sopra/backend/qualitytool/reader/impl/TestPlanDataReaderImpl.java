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
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.model.source.TestPlanSourceFileDto;
import com.sopra.backend.qualitytool.reader.TestPlanDataReader;

@Service
public class TestPlanDataReaderImpl implements TestPlanDataReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutionDataReaderImpl.class);
	private static final int NO_COLUMNS = 4;

	/**
	 * Read and verify the contents of the testPlanSourceFile if it is ok to be
	 * processed further
	 * 
	 * @param file
	 * @param testPlanSourceFileDto
	 * @return spreadsheet
	 */
	@Override
	public XSSFSheet readTestPlanFile(String file, TestPlanSourceFileDto testPlanSourceFileDto) {
		XSSFSheet spreadsheet = null;
		try (FileInputStream fis = new FileInputStream(new File(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fis);) {
			spreadsheet = workbook.getSheetAt(ApplicationConstants.TEST_PLAN_FILE_SHEET_INDEX);
			/* verifying column indices to process the file ahead */
			Boolean flagColumnIndex = verifyColumnIndices(spreadsheet, testPlanSourceFileDto);
			if (!flagColumnIndex) {
				return null;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return spreadsheet;
	}

	/**
	 * Check if the Test Plan file has all the columns required to process the
	 * file .
	 * 
	 * @param spreadsheet
	 * @param testPlanSourceFileDto
	 * @return flagFound
	 */
	private boolean verifyColumnIndices(XSSFSheet spreadsheet, TestPlanSourceFileDto testPlanSourceFileDto) {
		Boolean flagFound = Boolean.FALSE;
		Integer countColumnIndex = 0;

		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.equalsIgnoreCase(ApplicationConstants.TEST_PLAN_GCU_COLUMN_REGEX)) {
						testPlanSourceFileDto.setGcuColumnIndex(cell.getColumnIndex());
						testPlanSourceFileDto.setSourceFileFirstRow(cell.getRowIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.PACK_COLUMN_REGEX)) {
						testPlanSourceFileDto.setPackColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.DEFECT_COLUMN_REGEX)) {
						testPlanSourceFileDto.setDefectColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.CLASS_OF_BUG_COLUMN_REGEX)) {
						testPlanSourceFileDto.setClassOfBugColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					}
				}
			}
		}
		if (countColumnIndex == NO_COLUMNS) {
			flagFound = true;
		}else{
			LOGGER.error("Column Indices not found !!");
		}
		return flagFound;
	}

	/**
	 * Process Test Plan File Data to be added to the Quality File
	 * 
	 * @param sheet
	 * @param testPlanSourceFileDto
	 * @param listQualityDataDto
	 * 
	 */
	@Override
	public void filteringData(XSSFSheet sheet, TestPlanSourceFileDto testPlanSourceFileDto,
			List<QualityDataDto> listQualityDataDto) {
		if (Objects.nonNull(testPlanSourceFileDto)) {
			int blocker = 0;
			int major = 0;
			int minor = 0;
			Integer startingRow = testPlanSourceFileDto.getSourceFileFirstRow();
			Integer gcuColumnIndex = testPlanSourceFileDto.getGcuColumnIndex();
			String gcuName = sheet.getRow(startingRow + 1).getCell(gcuColumnIndex).getStringCellValue();
			Boolean lastRow = Boolean.FALSE;
			for (int rowIndex = startingRow + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (Objects.nonNull(row) && Objects.nonNull(row.getCell(gcuColumnIndex))
						&& Objects.nonNull(row.getCell(gcuColumnIndex).getStringCellValue())
						&& !(row.getCell(gcuColumnIndex).getStringCellValue().trim().isEmpty())) {
					String currentGcu = row.getCell(gcuColumnIndex).getStringCellValue().trim();
					Integer rowNum = row.getRowNum();
					if ((rowNum == sheet.getLastRowNum()
							|| Objects.isNull(sheet.getRow(rowNum + 1).getCell(gcuColumnIndex))
							|| sheet.getRow(rowNum + 1).getCell(gcuColumnIndex).getStringCellValue().trim()
									.isEmpty())) {
						lastRow = true;
					}

					if (rowNum == startingRow + 1 || currentGcu.equalsIgnoreCase(gcuName)) {
						Cell cell = row.getCell(testPlanSourceFileDto.getDefectColumnIndex());
						if (Objects.nonNull(cell) && cell.getStringCellValue().equalsIgnoreCase("D")) {
							Cell classOfBugCell = row.getCell(testPlanSourceFileDto.getClassOfBugColumnIndex());
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
							String packName = row.getCell(testPlanSourceFileDto.getPackColumnIndex())
									.getStringCellValue().trim();
							addDtoToList(listQualityDataDto, gcuName, packName, blocker, major, minor);
						}

					} else if (!(currentGcu.equalsIgnoreCase(gcuName))) {
						// adding previous gcu to list of Dto
						String packName = sheet.getRow(rowNum - 1).getCell(testPlanSourceFileDto.getPackColumnIndex())
								.getStringCellValue().trim();
						addDtoToList(listQualityDataDto, gcuName, packName, blocker, major, minor);
						major = blocker = minor = 0;
						gcuName = currentGcu;
						Cell cell = row.getCell(testPlanSourceFileDto.getDefectColumnIndex());
						if (Objects.nonNull(cell) && cell.getStringCellValue().equalsIgnoreCase("D")) {
							Cell classOfBugCell = row.getCell(testPlanSourceFileDto.getClassOfBugColumnIndex());
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
							packName = row.getCell(testPlanSourceFileDto.getPackColumnIndex()).getStringCellValue()
									.trim();
							addDtoToList(listQualityDataDto, gcuName, packName, blocker, major, minor);
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
	 * @param gcuName
	 * @param packName
	 * @param blocker
	 * @param major
	 * @param minor
	 */
	private void addDtoToList(List<QualityDataDto> listQualityData, String gcuName, String packName, Integer blocker,
			Integer major, Integer minor) {
		QualityDataDto qualityData = new QualityDataDto();
		qualityData.setBlocker(blocker);
		qualityData.setGroup(gcuName);
		qualityData.setMajor(major);
		qualityData.setMinor(minor);
		qualityData.setPhase(ApplicationConstants.TEST_PLAN_PHASE);
		qualityData.setPack(ApplicationConstants.PACK_NAME_REGEX + packName.charAt(packName.length() - 1));
		listQualityData.add(qualityData);
	}

}