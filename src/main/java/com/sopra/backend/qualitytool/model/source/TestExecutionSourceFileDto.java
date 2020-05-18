package com.sopra.backend.qualitytool.model.source;

public class TestExecutionSourceFileDto {
	/* The first cell of source file containing the column headers */
	private Integer sourceFileFirstRow;
	private Integer gcuColumnIndex;
	private Integer priorityColumnIndex;

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

	public Integer getPriorityColumnIndex() {
		return priorityColumnIndex;
	}

	public void setPriorityColumnIndex(Integer priorityColumnIndex) {
		this.priorityColumnIndex = priorityColumnIndex;
	}

}
