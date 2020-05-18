package com.sopra.backend.qualitytool.dto;


public class FileDto {

    private String sfdReviewFilePath;
    private String designReviewFilePath;
    private String qualityDataFilePath;
    
	public String getSfdReviewFilePath() {
		return sfdReviewFilePath;
	}
	public void setSfdReviewFilePath(String sfdReviewFilePath) {
		this.sfdReviewFilePath = sfdReviewFilePath;
	}
	public String getDesignReviewFilePath() {
		return designReviewFilePath;
	}
	public void setDesignReviewFilePath(String designReviewFilePath) {
		this.designReviewFilePath = designReviewFilePath;
	}
	public String getQualityDataFilePath() {
		return qualityDataFilePath;
	}
	public void setQualityDataFilePath(String qualityDataFilePath) {
		this.qualityDataFilePath = qualityDataFilePath;
	}
	

}
