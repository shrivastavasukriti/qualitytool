package com.sopra.backend.qualitytool.reader;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sopra.backend.qualitytool.model.source.DesignReviewSourceFileDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;

public interface DesignReviewDataReader {

	public XSSFSheet readDesignReviewFile(String file, DesignReviewSourceFileDto designReviewSourceFile);

	public void filteringData(XSSFSheet sheet, DesignReviewSourceFileDto designReviewSourceFile,
			List<QualityDataDto> listQualityData);

}