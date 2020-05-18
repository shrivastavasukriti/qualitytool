package com.sopra.backend.qualitytool.reader;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.source.EffortsDto;
import com.sopra.backend.qualitytool.model.source.SuiviCpSourceFileDto;

public interface SuiviCpDataReader {

	public List<XSSFSheet> readSuiviCpFile(String file, List<SuiviCpSourceFileDto> suiviCpSourceFileDtoList);

	public void filteringData(List<XSSFSheet> spreadSheetList, List<SuiviCpSourceFileDto> suiviCpSourceFileDtoList,
			List<EffortsDto> effortsDtoList);

}
