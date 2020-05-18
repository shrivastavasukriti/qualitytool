package com.sopra.backend.qualitytool.reader;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.source.DesignReviewSourceFileDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;

public interface DesignReviewDataReader {

	public XSSFSheet readDesignReviewFile(String file,DesignReviewSourceFileDto designReviewSourceFile);
    
	public Boolean populateQualityDataDto(XSSFSheet spreadsheet,DesignReviewSourceFileDto designReviewSourceFile,QualityDataDto qualityDataDto);
	
	public void filteringData(XSSFSheet sheet,DesignReviewSourceFileDto designReviewSourceFile,QualityDataDto qualityDataDto);

}