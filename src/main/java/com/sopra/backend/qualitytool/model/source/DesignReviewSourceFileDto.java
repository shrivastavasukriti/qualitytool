package com.sopra.backend.qualitytool.model.source;

public class DesignReviewSourceFileDto {
	/*
	 * The first cell of source file containing the column headers
	 */
	private Integer sourceFileFirstRow;
	private Integer packColumnIndex;
	private Integer groupColumnIndex;
	private Integer branchColumnIndex;
	private Integer defectColumnIndex;
	private Integer classOfBugColumnIndex;

	public Integer getSourceFileFirstRow() {
		return sourceFileFirstRow;
	}

	public void setSourceFileFirstRow(Integer sourceFileFirstRow) {
		this.sourceFileFirstRow = sourceFileFirstRow;
	}

	public Integer getPackColumnIndex() {
		return packColumnIndex;
	}

	public void setPackColumnIndex(Integer packColumnIndex) {
		this.packColumnIndex = packColumnIndex;
	}

	public Integer getGroupColumnIndex() {
		return groupColumnIndex;
	}

	public void setGroupColumnIndex(Integer groupColumnIndex) {
		this.groupColumnIndex = groupColumnIndex;
	}

	public Integer getBranchColumnIndex() {
		return branchColumnIndex;
	}

	public void setBranchColumnIndex(Integer branchColumnIndex) {
		this.branchColumnIndex = branchColumnIndex;
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
		return "InformationOfSourceFile [sourceFileFirstRow=" + sourceFileFirstRow + ", packColumnIndex="
				+ packColumnIndex + ", groupColumnIndex=" + groupColumnIndex + ", branchColumnIndex="
				+ branchColumnIndex + ", defectColumnIndex=" + defectColumnIndex + ", classOfBugColumnIndex="
				+ classOfBugColumnIndex + "]";
	}

}
