package com.sopra.backend.qualitytool.dto;


public class FileDto {

    private String sfdReviewFilePath;
    private String designReviewFilePath;
    
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

}
