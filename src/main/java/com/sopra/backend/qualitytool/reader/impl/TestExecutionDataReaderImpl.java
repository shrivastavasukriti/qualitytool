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
import com.sopra.backend.qualitytool.model.source.TestExecutionSourceFileDto;
import com.sopra.backend.qualitytool.reader.TestExecutionDataReader;

@Service
public class TestExecutionDataReaderImpl implements TestExecutionDataReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutionDataReaderImpl.class);
	private static final int NO_COLUMNS = 2;

	/**
	 * Read and verify the contents of the testExecutionSourceFile if it is ok
	 * to be processed further
	 * 
	 * @param file
	 * @param testExecutionSourceFileDto
	 * @return spreadsheet
	 */
	@Override
	public XSSFSheet readTestExecutionFile(String file, TestExecutionSourceFileDto testExecutionSourceFileDto) {
		XSSFSheet spreadsheet = null;
		try (FileInputStream fis = new FileInputStream(new File(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fis);) {
			spreadsheet = workbook.getSheetAt(ApplicationConstants.TEST_EXECUTION_FILE_SHEET_INDEX);
			/* verifying column indices to process the file ahead */
			Boolean flagColumnIndex = verifyColumnIndices(spreadsheet, testExecutionSourceFileDto);
			if (flagColumnIndex) {
				return spreadsheet;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return spreadsheet;
	}

	/**
	 * Check if the Test Execution file has all the columns required to process
	 * the file .
	 * 
	 * @param spreadsheet
	 * @param testExecutionSourceFileDto
	 * @return flagFound
	 */
	private boolean verifyColumnIndices(XSSFSheet spreadsheet, TestExecutionSourceFileDto testExecutionSourceFileDto) {
		Boolean flagFound = Boolean.FALSE;
		Integer countColumnIndex = 0;

		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.equalsIgnoreCase(ApplicationConstants.GCU_NAME_REGEX)) {
						testExecutionSourceFileDto.setGcuColumnIndex(cell.getColumnIndex());
						testExecutionSourceFileDto.setSourceFileFirstRow(cell.getRowIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.PRIORITY_NAME_REGEX)) {
						testExecutionSourceFileDto.setPriorityColumnIndex(cell.getColumnIndex());
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
	 * Process Test Execution File Data to be added to the Quality File
	 * 
	 * @param sheet
	 * @param testExecutionSourceFileDto
	 * @param listQualityDataDto
	 * 
	 */
	@Override
	public void filteringData(XSSFSheet sheet, TestExecutionSourceFileDto testExecutionSourceFileDto,
			List<QualityDataDto> listQualityDataDto) {
		if (Objects.nonNull(testExecutionSourceFileDto)) {
			int blocker = 0;
			int major = 0;
			int minor = 0;
			Integer startingRow = testExecutionSourceFileDto.getSourceFileFirstRow();
			Integer gcuColumnIndex = testExecutionSourceFileDto.getGcuColumnIndex();
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
						Cell cell = row.getCell(testExecutionSourceFileDto.getPriorityColumnIndex());
						if (Objects.nonNull(cell) && Objects.nonNull(cell.getStringCellValue())
								&& !(cell.getStringCellValue().trim().isEmpty())) {
							switch (cell.getStringCellValue().trim().charAt(0)) {
							case '3':
								major++;
								break;
							case '4':
								minor++;
								break;
							case '1':
								blocker++;
								break;
							}
						}
						if (lastRow) {
							addDtoToList(listQualityDataDto, gcuName, blocker, major, minor);
						}

					} else if (!(currentGcu.equalsIgnoreCase(gcuName))) {
						// adding previous gcu to list of Dto
						addDtoToList(listQualityDataDto, gcuName, blocker, major, minor);
						major = blocker = minor = 0;
						gcuName = currentGcu;
						Cell cell = row.getCell(testExecutionSourceFileDto.getPriorityColumnIndex());
						if (Objects.nonNull(cell) && Objects.nonNull(cell.getStringCellValue())
								&& !(cell.getStringCellValue().trim().isEmpty())) {
							switch (cell.getStringCellValue().trim().charAt(0)) {
							case '3':
								major++;
								break;
							case '4':
								minor++;
								break;
							case '1':
								blocker++;
								break;
							}
						}
						if (lastRow) {
							addDtoToList(listQualityDataDto, gcuName, blocker, major, minor);
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
	 * @param blocker
	 * @param major
	 * @param minor
	 */
	private void addDtoToList(List<QualityDataDto> listQualityData, String gcuName, Integer blocker, Integer major,
			Integer minor) {
		QualityDataDto qualityData = new QualityDataDto();
		qualityData.setBlocker(blocker);
		qualityData.setGroup(gcuName);
		qualityData.setMajor(major);
		qualityData.setMinor(minor);
		qualityData.setPhase(ApplicationConstants.TEST_EXECUTION_PHASE);
		listQualityData.add(qualityData);
	}

}