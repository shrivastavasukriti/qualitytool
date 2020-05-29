package com.sopra.backend.qualitytool.writer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
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
import com.sopra.backend.qualitytool.model.destination.QualityDataColumnsDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.writer.QualityDataWriter;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QualityDataWriterImpl implements QualityDataWriter {

	private static final String DATE_FORMAT = "dd-mm-yyyy";

	private static final Logger LOGGER = LoggerFactory.getLogger(QualityDataWriterImpl.class);

	private static final int NUMBER_OF_COLUMNS_VERIFIED = 11;

	private volatile XSSFWorkbook workbook;

	/**
	 * Read and verify the contents of the qualityDataFile, if it is ok to be
	 * processed further
	 * 
	 * @param file
	 * @param qualityDataColumnsDto
	 * @return spreadsheet
	 */
	@Override
	public XSSFSheet readQualityFile(String file, QualityDataColumnsDto qualityDataColumnsDto) {
		XSSFSheet spreadsheet = null;
		try (FileInputStream fis = new FileInputStream(new File(file));) {
			workbook = new XSSFWorkbook(fis);
			spreadsheet = workbook.getSheetAt(ApplicationConstants.QUALITY_SHEET_NUMBER);
			Boolean flagColumnFound = verifyColumnIndices(spreadsheet, qualityDataColumnsDto);
			if (!flagColumnFound) {
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
	 * @param qualityDataColumnsDto
	 * @return flagColumnFound
	 */
	private Boolean verifyColumnIndices(XSSFSheet spreadsheet, QualityDataColumnsDto qualityDataColumnsDto) {
		Boolean flagColumnFound = Boolean.FALSE;
		Integer countColumnIndex = 0;

		for (Row row : spreadsheet) {
			for (Cell cell : row) {
				if (Objects.nonNull(cell) && cell.getCellTypeEnum() == CellType.STRING) {
					String cellValue = cell.getStringCellValue().trim();
					if (cellValue.equalsIgnoreCase(ApplicationConstants.PACK_REGEX_WRITER)) {
						qualityDataColumnsDto.setPackColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.MODULE_REGEX_WRITER)) {
						qualityDataColumnsDto.setModuleColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.GROUP_REGEX_WRITER)) {
						qualityDataColumnsDto.setGroupColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.CUF_REGEX_WRITER)) {
						qualityDataColumnsDto.setCufColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.PHASE_REGEX_WRITER)) {
						qualityDataColumnsDto.setPhaseColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.contains(ApplicationConstants.EFFORTS_REGEX_WRITER)) {
						qualityDataColumnsDto.setEffortsColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.BLOCKER_REGEX_WRITER)) {
						qualityDataColumnsDto.setBlockerColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.MAJOR_REGEX_WRITER)) {
						qualityDataColumnsDto.setMajorColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.MINOR_REGEX_WRITER)) {
						qualityDataColumnsDto.setMinorColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.DDBD_REGEX_WRITER)) {
						qualityDataColumnsDto.setDdbdColumnIndex(cell.getColumnIndex());
						countColumnIndex++;

					} else if (cellValue.equalsIgnoreCase(ApplicationConstants.RECORD_UPDATED_REGEX_WRITER)) {
						qualityDataColumnsDto.setRecordUpdatedColumnIndex(cell.getColumnIndex());
						countColumnIndex++;
					}
				}
			}
		}
		if (countColumnIndex == NUMBER_OF_COLUMNS_VERIFIED) {
			flagColumnFound = Boolean.TRUE;
		}
		return flagColumnFound;
	}

	

	/**
	 * Update or Create a Record for writing to Quality Data files
	 * 
	 * @param qualityDataColumnsDto
	 * @param spreadsheet
	 * @param listQualityDataDto
	 * 
	 */
	
	@Override
	public void updateOrCreateRecord(QualityDataColumnsDto qualityDataColumnsDto, XSSFSheet spreadsheet,
			List<QualityDataDto> listQualityDataDto) {
		Boolean flagUpdateRecord;
		for (QualityDataDto qualityDataDto : listQualityDataDto) {
			flagUpdateRecord = Boolean.FALSE;
			for (Row row : spreadsheet) {
				Cell cellGroup = row.getCell(qualityDataColumnsDto.getGroupColumnIndex());
				Cell cellPhase = row.getCell(qualityDataColumnsDto.getPhaseColumnIndex());
				if (Objects.nonNull(cellGroup) && Objects.nonNull(cellPhase)) {
					if (cellGroup.getRichStringCellValue().getString().trim()
							.equalsIgnoreCase(qualityDataDto.getGroup())
							&& cellPhase.getRichStringCellValue().getString().trim()
									.equalsIgnoreCase(qualityDataDto.getPhase())) {
						
						Double efforts = qualityDataDto.getEffort();
						if(Objects.nonNull(efforts)){
							row.getCell(qualityDataColumnsDto.getEffortsColumnIndex()).setCellValue(efforts);
						}
						if(Objects.nonNull(efforts) && efforts == 0){
							row.getCell(qualityDataColumnsDto.getDdbdColumnIndex()).setCellType(CellType.BLANK);
						}
						
						if(Objects.nonNull(qualityDataDto.getBlocker())){
							row.getCell(qualityDataColumnsDto.getBlockerColumnIndex())
							.setCellValue(qualityDataDto.getBlocker());
						}else{
							row.getCell(qualityDataColumnsDto.getBlockerColumnIndex())
							.setCellValue(0);
						}
						
						if(Objects.nonNull(qualityDataDto.getMajor())){
							row.getCell(qualityDataColumnsDto.getMajorColumnIndex())
							.setCellValue(qualityDataDto.getMajor());
						}else{
							row.getCell(qualityDataColumnsDto.getMajorColumnIndex())
							.setCellValue(0);
						}
						
						if(Objects.nonNull(qualityDataDto.getMinor())){
							row.getCell(qualityDataColumnsDto.getMinorColumnIndex())
							.setCellValue(qualityDataDto.getMinor());
						}else{
							row.getCell(qualityDataColumnsDto.getMinorColumnIndex())
							.setCellValue(0);
						}
						
						CellStyle originalStyle = row.getCell(1).getCellStyle();
						CellStyle cellStyle = workbook.createCellStyle();
						CreationHelper createHelper = workbook.getCreationHelper();
						cellStyle.cloneStyleFrom(originalStyle);
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						row.getCell(qualityDataColumnsDto.getRecordUpdatedColumnIndex())
								.setCellValue(calendar.getTime());
						row.getCell(qualityDataColumnsDto.getRecordUpdatedColumnIndex()).setCellStyle(cellStyle);
						flagUpdateRecord = Boolean.TRUE;
					}
				}
			}
			if (!flagUpdateRecord) {
				/*
				 * If group or phase for that group doesn't exist in excel then
				 * create new row
				 */
				createNewRecord(qualityDataDto, qualityDataColumnsDto, spreadsheet);

			}
		}

	}

	
	/**
	 * Creates new row in Quality Data if Group and/or Phase not found
	 * 
	 * @param qualityDataDto
	 * @param qualityDataColumnsDto
	 * @param spreadsheet
	 * 
	 */
	private void createNewRecord(QualityDataDto qualityDataDto, QualityDataColumnsDto qualityDataColumnsDto,
			XSSFSheet spreadsheet) {
		XSSFRow lastRow = spreadsheet.getRow(spreadsheet.getLastRowNum());
		XSSFRow newRow = spreadsheet.createRow(lastRow.getRowNum() + 1);
		CellStyle originalStringStyle = lastRow.getCell(qualityDataColumnsDto.getPackColumnIndex()).getCellStyle();
		CellStyle originalNumericStyle = lastRow.getCell(qualityDataColumnsDto.getBlockerColumnIndex()).getCellStyle();
		CellStyle cellStyle = workbook.createCellStyle();
		CellStyle numericCellStyle = workbook.createCellStyle();
		for (Integer cellid = 0; cellid < lastRow.getLastCellNum(); cellid++) {
			Cell cell = newRow.createCell(cellid);
			if (cellid == qualityDataColumnsDto.getPackColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getPack());
			} else if (cellid == qualityDataColumnsDto.getModuleColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getModule());
			} else if (cellid == qualityDataColumnsDto.getGroupColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getGroup());
			} else if (cellid == qualityDataColumnsDto.getPhaseColumnIndex()) {
				cell.setCellValue((String) qualityDataDto.getPhase());
			} else if (cellid == qualityDataColumnsDto.getEffortsColumnIndex()) {
				
				Double efforts = qualityDataDto.getEffort();
				if (efforts != null) {
					cell.setCellValue(efforts);
				}
			} else if (cellid == qualityDataColumnsDto.getBlockerColumnIndex() && Objects.nonNull(qualityDataDto.getBlocker())) {
				cell.setCellValue(qualityDataDto.getBlocker());
			} else if (cellid == qualityDataColumnsDto.getMajorColumnIndex() && Objects.nonNull(qualityDataDto.getMajor())) {
				cell.setCellValue(qualityDataDto.getMajor());
			} else if (cellid == qualityDataColumnsDto.getMinorColumnIndex() && Objects.nonNull(qualityDataDto.getMinor())) {
				cell.setCellValue(qualityDataDto.getMinor());
			} else if (cellid == qualityDataColumnsDto.getDdbdColumnIndex()) {
				CellAddress blockerCellAddress = newRow.getCell(qualityDataColumnsDto.getBlockerColumnIndex())
						.getAddress();
				CellAddress majorCellAddress = newRow.getCell(qualityDataColumnsDto.getMajorColumnIndex()).getAddress();
				CellAddress minorCellAddress = newRow.getCell(qualityDataColumnsDto.getMinorColumnIndex()).getAddress();
				CellAddress effortsCellAddress = newRow.getCell(qualityDataColumnsDto.getEffortsColumnIndex())
						.getAddress();
				if(Objects.nonNull(newRow.getCell(qualityDataColumnsDto.getEffortsColumnIndex()).getNumericCellValue()) && newRow.getCell(qualityDataColumnsDto.getEffortsColumnIndex()).getNumericCellValue()!= 0){
					cell.setCellFormula("(" + blockerCellAddress + "*1+" + majorCellAddress + "*0.5+" + minorCellAddress
							+ "*0.25)/" + effortsCellAddress + "*10");
				}
			} else if (cellid == qualityDataColumnsDto.getRecordUpdatedColumnIndex()) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				cell.setCellValue(calendar.getTime());
			}

			if (cellid == qualityDataColumnsDto.getBlockerColumnIndex()
					|| cellid == qualityDataColumnsDto.getMajorColumnIndex()
					|| cellid == qualityDataColumnsDto.getMinorColumnIndex()
					|| cellid == qualityDataColumnsDto.getEffortsColumnIndex()
					|| cellid == qualityDataColumnsDto.getDdbdColumnIndex()) {
				numericCellStyle.cloneStyleFrom(originalNumericStyle);
				cell.setCellStyle(numericCellStyle);

			} else if (cellid == qualityDataColumnsDto.getRecordUpdatedColumnIndex()) {
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyle.cloneStyleFrom(originalStringStyle);
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
				cell.setCellStyle(cellStyle);
			} else {
				cellStyle.cloneStyleFrom(originalStringStyle);
				cell.setCellStyle(cellStyle);
			}
		}
	}

	/**
	 * Write to file
	 * 
	 * @param outFilePath
	 * @return flagWrite
	 */
	@Override
	public Boolean writeToFile(String outFilePath) {
		Boolean flagWrite = Boolean.FALSE;
		try (FileOutputStream out = new FileOutputStream(new File(outFilePath));) {
			workbook.write(out);
			flagWrite = Boolean.TRUE;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return flagWrite;
	}
	
	

}
