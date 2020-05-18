package com.sopra.backend.qualitytool.writer;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.destination.QualityDataWriteDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;

public interface QualityDataWriter {

	public XSSFSheet readQualityFile(String file, QualityDataDto qualityDataDto);
	 
	public Boolean updateOrCreateRecord(QualityDataDto qualityDataDto,QualityDataWriteDto qualityDataWriteDto, XSSFSheet spreadsheet);
	
	public Boolean writeToFile(String outFilePath);

}