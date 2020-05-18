package com.sopra.backend.qualitytool.model.source;

public class SuiviCpSourceFileDto {
	/* The first cell of source file containing the column headers */
	private Integer sourceFileFirstRow;
	private Integer atterrissageSFDColumnIndex;
	private Integer ConsoColumnIndex;
	private Integer sfdRowIndex;
	private Integer designRowIndex;
	private Integer codeRowIndex;
	private Integer selectDataRowIndex;
	private Integer writeTestRowIndex;
	private Integer executeTestRowIndex;
	private Integer fixBugsRowIndex;

	public Integer getSourceFileFirstRow() {
		return sourceFileFirstRow;
	}

	public void setSourceFileFirstRow(Integer sourceFileFirstRow) {
		this.sourceFileFirstRow = sourceFileFirstRow;
	}

	public Integer getAtterrissageSFDColumnIndex() {
		return atterrissageSFDColumnIndex;
	}

	public void setAtterrissageSFDColumnIndex(Integer atterrissageSFDColumnIndex) {
		this.atterrissageSFDColumnIndex = atterrissageSFDColumnIndex;
	}

	public Integer getConsoColumnIndex() {
		return ConsoColumnIndex;
	}

	public void setConsoColumnIndex(Integer consoColumnIndex) {
		ConsoColumnIndex = consoColumnIndex;
	}

	public Integer getSfdRowIndex() {
		return sfdRowIndex;
	}

	public void setSfdRowIndex(Integer sfdRowIndex) {
		this.sfdRowIndex = sfdRowIndex;
	}

	public Integer getDesignRowIndex() {
		return designRowIndex;
	}

	public void setDesignRowIndex(Integer designRowIndex) {
		this.designRowIndex = designRowIndex;
	}

	public Integer getCodeRowIndex() {
		return codeRowIndex;
	}

	public void setCodeRowIndex(Integer codeRowIndex) {
		this.codeRowIndex = codeRowIndex;
	}

	public Integer getSelectDataRowIndex() {
		return selectDataRowIndex;
	}

	public void setSelectDataRowIndex(Integer selectDataRowIndex) {
		this.selectDataRowIndex = selectDataRowIndex;
	}

	public Integer getWriteTestRowIndex() {
		return writeTestRowIndex;
	}

	public void setWriteTestRowIndex(Integer writeTestRowIndex) {
		this.writeTestRowIndex = writeTestRowIndex;
	}

	public Integer getExecuteTestRowIndex() {
		return executeTestRowIndex;
	}

	public void setExecuteTestRowIndex(Integer executeTestRowIndex) {
		this.executeTestRowIndex = executeTestRowIndex;
	}

	public Integer getFixBugsRowIndex() {
		return fixBugsRowIndex;
	}

	public void setFixBugsRowIndex(Integer fixBugsRowIndex) {
		this.fixBugsRowIndex = fixBugsRowIndex;
	}

	@Override
	public String toString() {
		return "SuiviCpSourceFileDto [sourceFileFirstRow=" + sourceFileFirstRow + ", atterrissageSFDColumnIndex="
				+ atterrissageSFDColumnIndex + ", ConsoColumnIndex=" + ConsoColumnIndex + ", sfdRowIndex=" + sfdRowIndex
				+ ", designRowIndex=" + designRowIndex + ", codeRowIndex=" + codeRowIndex + ", selectDataRowIndex="
				+ selectDataRowIndex + ", writeTestRowIndex=" + writeTestRowIndex + ", executeTestRowIndex="
				+ executeTestRowIndex + ", fixBugsRowIndex=" + fixBugsRowIndex + "]";
	}

}
