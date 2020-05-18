package com.sopra.backend.qualitytool.reader;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.model.source.TestExecutionSourceFileDto;

public interface TestExecutionDataReader {

	public XSSFSheet readTestExecutionFile(String file, TestExecutionSourceFileDto testExecutionSourceFile);

	public void filteringData(XSSFSheet sheet, TestExecutionSourceFileDto testExecutionSourceFile,
			List<QualityDataDto> listQualityData);

}
