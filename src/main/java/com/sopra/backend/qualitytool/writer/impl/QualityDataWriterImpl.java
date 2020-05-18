package com.sopra.backend.qualitytool.writer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sopra.backend.qualitytool.constants.ApplicationConstants;
import com.sopra.backend.qualitytool.model.destination.QualityDataWriteDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.writer.QualityDataWriter;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QualityDataWriterImpl implements QualityDataWriter {

	private static final String DATE_FORMAT = "dd-mm-yyyy";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(QualityDataWriterImpl.class);

	private static final int NUMBER_OF_COLUMNS_VERIFIED = 11;

	private volatile XSSFWorkbook workbook;

	/**
	 * Read and verify the contents of the qualityDataFile, if it is ok to be
	 * processed further
	 * 
	 * @param file
	 * @param qualityDataDto
	 * @return spreadsheet
	 */
	@Override
	public XSSFSheet readQualityFile(String file, QualityDataDto qualityDataDto) {
		XSSFSheet spreadsheet = null;
		QualityDataWriteDto qualityDataWriteDto = new QualityDataWriteDto();
		try (FileInputStream fis = new FileInputStream(new File(file));) {
			workbook = new XSSFWorkbook(fis);
			spreadsheet = workbook
					.getSheetAt(ApplicationConstants.QUALITY_SHEET_NUMBER);
			Boolean flagColumnFound = verifyColumnIndices(spreadsheet,
					qualityDataWriteDto);
			if (flagColumnFound) {
				return spreadsheet;
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
	 * @param qualityDataWriteDto
	 * @return flagColumnFound
	 */
	private boolean verifyColumnIndices(XSSFSheet spreadsheet,
			QualityDataWriteDto qualityDataWriteDto) {
		Boolean flagColumnFound = Boolean.FALSE;
		Integer countColumnIndex = 0;

		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell)
						&& cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue
							.equalsIgnoreCase(ApplicationConstants.PACK_REGEX_WRITER)) {
						qualityDataWriteDto.setPackColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;
					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.MODULE_REGEX_WRITER)) {
						qualityDataWriteDto.setModuleColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.GROUP_REGEX_WRITER)) {
						qualityDataWriteDto.setGroupColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.CUF_REGEX_WRITER)) {
						qualityDataWriteDto.setCufColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.PHASE_REGEX_WRITER)) {
						qualityDataWriteDto.setPhaseColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.contains(ApplicationConstants.EFFORTS_REGEX_WRITER)) {
						qualityDataWriteDto.setEffortsColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.BLOCKER_REGEX_WRITER)) {
						qualityDataWriteDto.setBlockerColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.MAJOR_REGEX_WRITER)) {
						qualityDataWriteDto.setMajorColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.MINOR_REGEX_WRITER)) {
						qualityDataWriteDto.setMinorColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.DDBD_REGEX_WRITER)) {
						qualityDataWriteDto.setDdbdColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue
							.equalsIgnoreCase(ApplicationConstants.RECORD_UPDATED_REGEX_WRITER)) {
						qualityDataWriteDto.setRecordUpdatedColumnIndex(cell
								.getColumnIndex());
						countColumnIndex++;
					}
				}
			}
			if (countColumnIndex == NUMBER_OF_COLUMNS_VERIFIED) {
				flagColumnFound = Boolean.TRUE;
			}
		}
		return flagColumnFound;
	}

	/**
	 * Update or Create a Record for writing to Quality Data files 
	 * @param qualityDataDto
	 * @param qualityDataWriteDto
	 * @param spreadsheet
	 * @return flagUpdateRecord
	 */
	@Override
	public Boolean updateOrCreateRecord(QualityDataDto qualityDataDto,
			QualityDataWriteDto qualityDataWriteDto, XSSFSheet spreadsheet) {
		Boolean flagUpdateRecord =Boolean.FALSE;
		for (Row row : spreadsheet) {
			Cell cellGroup = row.getCell(qualityDataWriteDto.getGroupColumnIndex());
			Cell cellPhase = row.getCell(qualityDataWriteDto.getPhaseColumnIndex());
			if (Objects.nonNull(cellGroup) && Objects.nonNull(cellPhase)) {
				if (cellGroup.getRichStringCellValue().getString().trim()
						.equalsIgnoreCase(qualityDataDto.getGroup())
						&& cellPhase
								.getRichStringCellValue()
								.getString()
								.trim()
								.equalsIgnoreCase(
										qualityDataDto.getPhase())) {
					row.getCell(qualityDataWriteDto.getBlockerColumnIndex())
							.setCellValue(qualityDataDto.getBlocker());
					row.getCell(qualityDataWriteDto.getMajorColumnIndex())
							.setCellValue(qualityDataDto.getMajor());
					row.getCell(qualityDataWriteDto.getMinorColumnIndex())
							.setCellValue(qualityDataDto.getMinor());
					CellStyle originalStyle = row.getCell(1).getCellStyle();
					CellStyle cellStyle = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					cellStyle.cloneStyleFrom(originalStyle);
					cellStyle.setDataFormat(createHelper.createDataFormat()
							.getFormat(DATE_FORMAT));
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					row.getCell(qualityDataWriteDto.getRecordUpdatedColumnIndex())
							.setCellValue(calendar.getTime());
					row.getCell(qualityDataWriteDto.getRecordUpdatedColumnIndex())
							.setCellStyle(cellStyle);
					flagUpdateRecord = Boolean.TRUE;
				}
			}
		}
		if(!flagUpdateRecord){
			/* If group or phase for that group doesn't exist in excel then create new row */
			flagUpdateRecord = createNewRecord(qualityDataDto, qualityDataWriteDto, spreadsheet);
			
			
		}
		return flagUpdateRecord;
	}
    /**
     * 
     * @param qualityDataDto
     * @param qualityDataWriteDto
     * @param spreadsheet
     * @return
     */
	private Boolean createNewRecord(QualityDataDto qualityDataDto,
			QualityDataWriteDto qualityDataWriteDto, XSSFSheet spreadsheet) {
		XSSFRow lastRow = spreadsheet.getRow(spreadsheet.getLastRowNum());
		XSSFRow newRow = spreadsheet.createRow(lastRow.getRowNum() + 1);
		CellStyle originalStringStyle = lastRow.getCell(
				qualityDataWriteDto.getPackColumnIndex()).getCellStyle();
		CellStyle originalNumericStyle = lastRow.getCell(
				qualityDataWriteDto.getBlockerColumnIndex()).getCellStyle();
		CellStyle cellStyle = workbook.createCellStyle();
		CellStyle numericCellStyle = workbook.createCellStyle();
		Integer numberOfCellsIntserted = 0;
		for (Integer cellid = 0; cellid < lastRow.getLastCellNum(); cellid++) {
			Cell cell = newRow.createCell(cellid);
			if (cellid == qualityDataWriteDto.getPackColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getPack());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getModuleColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getModule());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getGroupColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getGroup());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getPhaseColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getPhase());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getEffortsColumnIndex()) {
				// setting a dummy value
				cell.setCellValue(20.5);
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getBlockerColumnIndex()) {
				cell.setCellValue(qualityDataDto.getBlocker());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getMajorColumnIndex()) {
				cell.setCellValue(qualityDataDto.getMajor());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getMinorColumnIndex()) {
				cell.setCellValue(qualityDataDto.getMinor());
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getDdbdColumnIndex()) {
				CellAddress blockerCellAddress = newRow.getCell(
						qualityDataWriteDto.getBlockerColumnIndex()).getAddress();
				CellAddress majorCellAddress = newRow.getCell(
						qualityDataWriteDto.getMajorColumnIndex()).getAddress();
				CellAddress minorCellAddress = newRow.getCell(
						qualityDataWriteDto.getMinorColumnIndex()).getAddress();
				CellAddress effortsCellAddress = newRow.getCell(
						qualityDataWriteDto.getEffortsColumnIndex()).getAddress();
				cell.setCellFormula("(" + blockerCellAddress + "*1+"
						+ majorCellAddress + "*0.5+" + minorCellAddress
						+ "*0.25)/" + effortsCellAddress + "*10");
				numberOfCellsIntserted++;
			} else if (cellid == qualityDataWriteDto.getRecordUpdatedColumnIndex()) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				cell.setCellValue(calendar.getTime());
				numberOfCellsIntserted++;
			}

			if (cellid == qualityDataWriteDto.getBlockerColumnIndex()
					|| cellid == qualityDataWriteDto.getMajorColumnIndex()
					|| cellid == qualityDataWriteDto.getMinorColumnIndex()
					|| cellid == qualityDataWriteDto.getEffortsColumnIndex()
					|| cellid == qualityDataWriteDto.getDdbdColumnIndex()) {
				numericCellStyle.cloneStyleFrom(originalNumericStyle);
				cell.setCellStyle(numericCellStyle);

			} else if (cellid == qualityDataWriteDto.getRecordUpdatedColumnIndex()) {
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyle.cloneStyleFrom(originalStringStyle);
				cellStyle.setDataFormat(createHelper.createDataFormat()
						.getFormat(DATE_FORMAT));
				cell.setCellStyle(cellStyle);
			} else {
				cellStyle.cloneStyleFrom(originalStringStyle);
				cell.setCellStyle(cellStyle);
			}
		}
		if (numberOfCellsIntserted == 10) {
			return true;
		} 
		return false;
	}
	/**
	 * Write to file 
	 * @param outFilePath
	 * @return
	 */
	@Override
	public Boolean writeToFile(String outFilePath) {
		Boolean flagWrite=Boolean.FALSE;		
		try(FileOutputStream out= new FileOutputStream(new File(outFilePath));) {
			workbook.write(out);
			flagWrite=Boolean.TRUE;					
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return flagWrite;
	}

}
