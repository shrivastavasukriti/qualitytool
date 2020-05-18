package com.sopra.backend.qualitytool.writer;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.destination.QualityDataColumnsDto;
import com.sopra.backend.qualitytool.model.source.EffortsDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;

public interface QualityDataWriter {

	public XSSFSheet readQualityFile(String file, QualityDataColumnsDto qualityDataWriteDto);

	public List<Boolean> updateOrCreateRecord(QualityDataColumnsDto qualityDataColumnsDto, XSSFSheet spreadsheet,
			List<QualityDataDto> listQualityDataDto, List<EffortsDto> listEffortsDto, String phase);

	public Boolean writeToFile(String outFilePath);

}