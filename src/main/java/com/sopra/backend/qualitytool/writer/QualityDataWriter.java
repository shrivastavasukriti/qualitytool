package com.sopra.backend.qualitytool.writer;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.destination.QualityDataColumnsDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;

public interface QualityDataWriter {

	public XSSFSheet readQualityFile(String file, QualityDataColumnsDto qualityDataWriteDto);

	public void updateOrCreateRecord(QualityDataColumnsDto qualityDataColumnsDto, XSSFSheet spreadsheet,
			List<QualityDataDto> listQualityDataDto);

	public Boolean writeToFile(String outFilePath);

}