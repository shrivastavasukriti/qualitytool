package com.sopra.backend.qualitytool.model.source;

public class TestPlanSourceFileDto {
	/* The first cell of source file containing the column headers */
	private Integer sourceFileFirstRow;
	private Integer gcuColumnIndex;
	private Integer packColumnIndex;
	private Integer defectColumnIndex;
	private Integer classOfBugColumnIndex;

	public Integer getSourceFileFirstRow() {
		return sourceFileFirstRow;
	}

	public void setSourceFileFirstRow(Integer sourceFileFirstRow) {
		this.sourceFileFirstRow = sourceFileFirstRow;
	}

	public Integer getGcuColumnIndex() {
		return gcuColumnIndex;
	}

	public void setGcuColumnIndex(Integer gcuColumnIndex) {
		this.gcuColumnIndex = gcuColumnIndex;
	}

	public Integer getPackColumnIndex() {
		return packColumnIndex;
	}

	public void setPackColumnIndex(Integer packColumnIndex) {
		this.packColumnIndex = packColumnIndex;
	}

	public Integer getDefectColumnIndex() {
		return defectColumnIndex;
	}

	public void setDefectColumnIndex(Integer defectColumnIndex) {
		this.defectColumnIndex = defectColumnIndex;
	}

	public Integer getClassOfBugColumnIndex() {
		return classOfBugColumnIndex;
	}

	public void setClassOfBugColumnIndex(Integer classOfBugColumnIndex) {
		this.classOfBugColumnIndex = classOfBugColumnIndex;
	}

	@Override
	public String toString() {
		return "TestPlanSourceFileDto [sourceFileFirstRow=" + sourceFileFirstRow + ", gcuColumnIndex=" + gcuColumnIndex
				+ ", packColumnIndex=" + packColumnIndex + ", defectColumnIndex=" + defectColumnIndex
				+ ", classOfBugColumnIndex=" + classOfBugColumnIndex + "]";
	}

}
