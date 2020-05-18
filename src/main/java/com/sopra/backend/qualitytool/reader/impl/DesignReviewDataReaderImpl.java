package com.sopra.backend.qualitytool.reader.impl;

import java.io.File;
import java.io.FileInputStream;
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
public class DesignReviewDataReaderImpl implements DesignReviewDataReader{
	private static final Logger LOGGER = LoggerFactory.getLogger(DesignReviewDataReaderImpl.class);
	
	private static final int NUMBER_OF_VAUES_UPDATED = 3;
	private static final int NO_COLUMNS = 5;

	/**
	 * Read and verify the contents of the designReviewFile if it is ok to be processed further
	 * @param file
	 * @param designReviewSourceFile
	 */
	@Override
	public XSSFSheet readDesignReviewFile(String file,DesignReviewSourceFileDto designReviewSourceFile){
		XSSFSheet spreadsheet = null;
		try(FileInputStream fis = new FileInputStream(new File(file));XSSFWorkbook workbook = new XSSFWorkbook(fis);) {		
			spreadsheet = workbook.getSheet(ApplicationConstants.FILE_SHEET);
				/* verifying column indices to process the file ahead*/
				Boolean flagColumnIndex = verifyColumnIndices(spreadsheet,designReviewSourceFile);
				if(flagColumnIndex){
					return spreadsheet;
				}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	  return spreadsheet;
	}
	
	/**
	 * Check if the Design Review file has all the columns required to process the file .
	 * @param spreadsheet
	 * @param designReviewSourceFileDto
	 * @return flagFound
	 */
	private boolean verifyColumnIndices(XSSFSheet spreadsheet,DesignReviewSourceFileDto designReviewSourceFileDto) {
		Boolean flagFound = false;
		Integer countColumnIndex = 0;

		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.matches(ApplicationConstants.RELEASE_REGEX)) {
						designReviewSourceFileDto.setPackColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.WORK_PRODUCT)) {
						designReviewSourceFileDto.setGroupColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.matches((ApplicationConstants.WORK_PRODUCT_REGEX))) {
						designReviewSourceFileDto.setBranchColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.REVIEW_TYPE_REGEX)) {
						designReviewSourceFileDto.setReviewTypeColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.CLASS_OF_BUGS)) {
						designReviewSourceFileDto.setClassOfBugColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					}
				}
			}
			if (countColumnIndex == NO_COLUMNS) {
				flagFound = true;
			}
		}
		return flagFound;
	}
	/**
	 * Initialize Design Review File Data 
	 * @param spreadsheet
	 * @param designReviewSourceFile
	 * @param qualityDataDto
	 */
    @Override
	public Boolean populateQualityDataDto(
			XSSFSheet spreadsheet,DesignReviewSourceFileDto designReviewSourceFile,QualityDataDto qualityDataDto) {
		Boolean flagValuesUpdated = Boolean.FALSE;
		Integer countValuesUpdated =0;
		for (Row row : spreadsheet) {
			Cell cell = row.getCell(designReviewSourceFile.getPackColumnIndex());
			if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING) {
				String cellValue = cell.getStringCellValue().trim();
				if (cellValue.substring(0, 4).equalsIgnoreCase(ApplicationConstants.PACK_REGEX)) {
					qualityDataDto.setPack(ApplicationConstants.PACK_NAME_REGEX + cellValue.trim().charAt(cellValue.length() - 1));
					designReviewSourceFile.setSourceFileFirstRow(row.getRowNum());
					countValuesUpdated++;
					if (Objects.nonNull(row.getCell(designReviewSourceFile.getGroupColumnIndex()))) {
						qualityDataDto.setGroup(
								row.getCell(designReviewSourceFile.getGroupColumnIndex()).getStringCellValue().trim());
						countValuesUpdated++;
					} 
					if (Objects.nonNull(row.getCell(designReviewSourceFile.getBranchColumnIndex()))) {
						cellValue = row.getCell(designReviewSourceFile.getBranchColumnIndex()).getStringCellValue()
								.trim();
						if (cellValue.contains(ApplicationConstants.DESIGN_REGEX)) {
							qualityDataDto.setPhase(ApplicationConstants.DESIGN_NAME_REGEX);
						} else {
							qualityDataDto.setPhase(ApplicationConstants.CODE_NAME_REGEX);
						}
						countValuesUpdated++;
					}
					if(countValuesUpdated == NUMBER_OF_VAUES_UPDATED){
						flagValuesUpdated = Boolean.TRUE;
					}
				}
			}
		}
		return flagValuesUpdated;
	}
    /**
     * Process Design Review File Data to be added to the Quality File
     * @param designReviewSourceFile
     * @param qualityDataDto
     * 
     */
	@Override
	public void filteringData(XSSFSheet sheet,DesignReviewSourceFileDto designReviewSourceFile,QualityDataDto qualityDataDto){
		int blocker = 0;
		int major = 0;
		int minor = 0;
		if (Objects.nonNull(designReviewSourceFile)) {
			for (Row row : sheet) {
				if (row.getRowNum() >= designReviewSourceFile.getSourceFileFirstRow()) {
					Cell cellReviewType = row.getCell(designReviewSourceFile.getReviewTypeColumnIndex());
					if (Objects.nonNull(cellReviewType)
							&& cellReviewType.getRichStringCellValue().getString().trim().equalsIgnoreCase("D")) {
						Cell cellClassOfBug = row.getCell(designReviewSourceFile.getClassOfBugColumnIndex());
						if (Objects.nonNull(cellClassOfBug)) {
							switch ((int) cellClassOfBug.getNumericCellValue()) {
							case 1:
								blocker++;
								break;
							case 2:
								major++;
								break;
							case 3:
								minor++;
								break;
							default:
								break;
							}
						}
					}
				}
			}
			qualityDataDto.setBlocker(blocker);
			qualityDataDto.setMajor(major);
			qualityDataDto.setMinor(minor);
		}

	}
}
