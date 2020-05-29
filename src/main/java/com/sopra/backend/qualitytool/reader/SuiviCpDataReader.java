package com.sopra.backend.qualitytool.reader;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.model.source.SuiviCpSourceFileDtoWrapper;

public interface SuiviCpDataReader {

	public List<XSSFSheet> readSuiviCpFile(String file, SuiviCpSourceFileDtoWrapper suiviCpSourceFileDtoWrapper);

	public void filteringData(List<XSSFSheet> spreadSheetList, SuiviCpSourceFileDtoWrapper suiviCpSourceFileDtoWrapper,
			List<QualityDataDto> qualityDataDtoList);

}
