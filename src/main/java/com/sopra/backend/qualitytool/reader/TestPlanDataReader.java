package com.sopra.backend.qualitytool.reader;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.model.source.TestPlanSourceFileDto;

public interface TestPlanDataReader {

	public XSSFSheet readTestPlanFile(String file, TestPlanSourceFileDto testPlanSourceFileDto);

	public void filteringData(XSSFSheet sheet, TestPlanSourceFileDto testPlanSourceFileDto,
			List<QualityDataDto> listQualityDataDto);

}
