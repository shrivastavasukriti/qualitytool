package com.sopra.backend.qualitytool.dto;

public class FileDto {

	private String sfdReviewFilePath;
	private String designReviewFilePath;
	private String qualityDataFilePath;
	private String testExecutionFilePath;
	private String suiviCpFilePath;
	private String testPlanFilePath;

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

	public String getTestExecutionFilePath() {
		return testExecutionFilePath;
	}

	public void setTestExecutionFilePath(String testExecutionFilePath) {
		this.testExecutionFilePath = testExecutionFilePath;
	}

	public String getSuiviCpFilePath() {
		return suiviCpFilePath;
	}

	public void setSuiviCpFilePath(String suiviCpFilePath) {
		this.suiviCpFilePath = suiviCpFilePath;
	}

	public String getTestPlanFilePath() {
		return testPlanFilePath;
	}

	public void setTestPlanFilePath(String testPlanFilePath) {
		this.testPlanFilePath = testPlanFilePath;
	}

	@Override
	public String toString() {
		return "FileDto [sfdReviewFilePath=" + sfdReviewFilePath + ", designReviewFilePath=" + designReviewFilePath
				+ ", qualityDataFilePath=" + qualityDataFilePath + ", testExecutionFilePath=" + testExecutionFilePath
				+ ", suiviCpFilePath=" + suiviCpFilePath + ", testPlanFilePath=" + testPlanFilePath + "]";
	}

}
