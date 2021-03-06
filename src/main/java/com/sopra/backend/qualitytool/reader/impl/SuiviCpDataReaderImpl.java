package com.sopra.backend.qualitytool.reader.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
import com.sopra.backend.qualitytool.model.source.SuiviCpSourceFileDto;
import com.sopra.backend.qualitytool.reader.SuiviCpDataReader;

@Service
public class SuiviCpDataReaderImpl implements SuiviCpDataReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(SuiviCpDataReaderImpl.class);
	private static final int NO_COLUMNS = 2;
	private static final int NO_PHASES =5;

	/**
	 * Read and verify the contents of the readSuiviCpFile if it is ok to be
	 * processed further
	 * 
	 * @param file
	 * @param suiviCpSourceFileDtoList
	 * @return spreadsheetList
	 */
	@Override
	public List<XSSFSheet> readSuiviCpFile(String file, List<SuiviCpSourceFileDto> suiviCpSourceFileDtoList) {
		List<XSSFSheet> spreadsheetList = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(new File(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fis);) {
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
				Boolean flagColumnIndex = verifyColumnAndRowIndices(sheet, suiviCpSourceFileDtoList);
				if (!flagColumnIndex) {
					return null;
				} else {
					spreadsheetList.add(sheet);
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return spreadsheetList;
	}

	/**
	 * Check if the SuiviCp file has all the columns and rows required to
	 * process the file .
	 * 
	 * @param spreadsheet
	 * @param suiviCpSourceFileDtoList
	 * @return flagFound
	 */
	private boolean verifyColumnAndRowIndices(XSSFSheet spreadsheet,
			List<SuiviCpSourceFileDto> suiviCpSourceFileDtoList) {
		Boolean flagFound = Boolean.FALSE;
		Integer countColumnIndex = 0;
		Integer countRowIndex = 0;
		SuiviCpSourceFileDto suiviCpSourceFileDto = new SuiviCpSourceFileDto();
		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING && countColumnIndex < 2) {

					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.equalsIgnoreCase(ApplicationConstants.ATTERRISSAGE_SFD_COLUMN_REGEX)) {
						suiviCpSourceFileDto.setAtterrissageSFDColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.CONSO_COLUMN_REGEX)) {
						suiviCpSourceFileDto.setConsoColumnIndex(cell.getColumnIndex());
						suiviCpSourceFileDto.setSourceFileFirstRow(cell.getRowIndex());
						countColumnIndex++;
					}
				}
			}
		}
		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING
						&& Objects.isNull(suiviCpSourceFileDto.getFixBugsRowIndex())) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.equalsIgnoreCase(ApplicationConstants.SFD_ROW_REGEX)) {
						suiviCpSourceFileDto.setSfdRowIndex(cell.getRowIndex());
						countRowIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.DESIGN_ROW_REGEX)) {
						suiviCpSourceFileDto.setDesignRowIndex(cell.getRowIndex());
						countRowIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.DEVELOPMENT_ROW_REGEX)) {
						suiviCpSourceFileDto.setCodeRowIndex(cell.getRowIndex());
						countRowIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.SELECT_DATA_ROW_REGEX)) {
						suiviCpSourceFileDto.setSelectDataRowIndex(cell.getRowIndex());
						countRowIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.WRITE_TEST_PLAN_ROW_REGEX)) {
						suiviCpSourceFileDto.setWriteTestRowIndex(cell.getRowIndex());
						countRowIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.EXECUTE_TEST_PLAN_ROW_REGEX)) {
						suiviCpSourceFileDto.setExecuteTestRowIndex(cell.getRowIndex());
						countRowIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.FIX_BUGS_ROW_REGEX)) {
						suiviCpSourceFileDto.setFixBugsRowIndex(cell.getRowIndex());
						countRowIndex++;
					}
				}
			}
		}
		if (countColumnIndex == NO_COLUMNS && Objects.nonNull(suiviCpSourceFileDto.getFixBugsRowIndex())) {
			suiviCpSourceFileDtoList.add(suiviCpSourceFileDto);
			flagFound = true;
		}else{
			LOGGER.error("Column Indices not found !!");
		}
		return flagFound;
	}

	/**
	 * Process SuiviCp File Data to calculate the effort(md) to be added to the
	 * Quality File
	 * 
	 * @param sheet
	 * @param suiviCpSourceFileDtoList
	 * @param effortsDtoList
	 * 
	 */
	@Override
	public void filteringData(List<XSSFSheet> spreadSheetList, List<SuiviCpSourceFileDto> suiviCpSourceFileDtoList,
			List<QualityDataDto> qualityDataDtoList) {

		for (int sheetIndex = 0; sheetIndex < spreadSheetList.size(); sheetIndex++) {
			XSSFSheet spreadSheet = spreadSheetList.get(sheetIndex);
			SuiviCpSourceFileDto suiviCpSourceFileDto = suiviCpSourceFileDtoList.get(sheetIndex);
			for (int cellIndex = suiviCpSourceFileDto.getAtterrissageSFDColumnIndex()
					+ 1; cellIndex < suiviCpSourceFileDto.getConsoColumnIndex(); cellIndex++) {

				for (int phaseNumber = 0; phaseNumber < NO_PHASES; phaseNumber++) {
					QualityDataDto qualityDataDto = new QualityDataDto();
					String[] packAndModule = spreadSheet.getSheetName().split("-");
					String packName = packAndModule[0].trim();
					qualityDataDto.setPack(ApplicationConstants.PACK_NAME_REGEX + packName.charAt(packName.length() - 1));
					qualityDataDto.setModule(packAndModule[1].trim());

					qualityDataDto.setGroup(spreadSheet.getRow(suiviCpSourceFileDto.getSourceFileFirstRow())
							.getCell(cellIndex).getStringCellValue());

					switch (phaseNumber) {
					case 0:
						/* SFD phase */
						qualityDataDto.setPhase(ApplicationConstants.SFD_PHASE);
						qualityDataDto.setEffort(spreadSheet.getRow(suiviCpSourceFileDto.getSfdRowIndex())
								.getCell(cellIndex).getNumericCellValue());
						break;
					case 1:
						/* Design phase */
						qualityDataDto.setPhase(ApplicationConstants.DESIGN_PHASE);
						qualityDataDto.setEffort(spreadSheet.getRow(suiviCpSourceFileDto.getDesignRowIndex())
								.getCell(cellIndex).getNumericCellValue());
						break;
					case 2:
						/* Code/Development phase */
						qualityDataDto.setPhase(ApplicationConstants.CODE_PHASE);
						qualityDataDto.setEffort(spreadSheet.getRow(suiviCpSourceFileDto.getCodeRowIndex())
								.getCell(cellIndex).getNumericCellValue());
						break;
					case 3:
						/* Test Plan phase */
						qualityDataDto.setPhase(ApplicationConstants.TEST_PLAN_PHASE);
						qualityDataDto.setEffort(spreadSheet.getRow(suiviCpSourceFileDto.getSelectDataRowIndex())
								.getCell(cellIndex).getNumericCellValue()
								+ spreadSheet.getRow(suiviCpSourceFileDto.getWriteTestRowIndex()).getCell(cellIndex)
										.getNumericCellValue());
						break;
					case 4:
						/* Test Execution phase */
						qualityDataDto.setPhase(ApplicationConstants.TEST_EXECUTION_PHASE);
						qualityDataDto.setEffort(spreadSheet.getRow(suiviCpSourceFileDto.getExecuteTestRowIndex())
								.getCell(cellIndex).getNumericCellValue()
								+ spreadSheet.getRow(suiviCpSourceFileDto.getFixBugsRowIndex()).getCell(cellIndex)
										.getNumericCellValue());
						break;

					}

					qualityDataDtoList.add(qualityDataDto);
				}

			}
		}
	}

}
