package com.sopra.backend.qualitytool.model.destination;

public class QualityDataColumnsDto {
	private Integer packColumnIndex;
	private Integer moduleColumnIndex;
	private Integer groupColumnIndex;
	private Integer cufColumnIndex;
	private Integer phaseColumnIndex;
	private Integer effortsColumnIndex;
	private Integer blockerColumnIndex;
	private Integer majorColumnIndex;
	private Integer minorColumnIndex;
	private Integer ddbdColumnIndex;
	private Integer recordUpdatedColumnIndex;

	public Integer getPackColumnIndex() {
		return packColumnIndex;
	}

	public void setPackColumnIndex(Integer packColumnIndex) {
		this.packColumnIndex = packColumnIndex;
	}

	public Integer getModuleColumnIndex() {
		return moduleColumnIndex;
	}

	public void setModuleColumnIndex(Integer moduleColumnIndex) {
		this.moduleColumnIndex = moduleColumnIndex;
	}

	public Integer getGroupColumnIndex() {
		return groupColumnIndex;
	}

	public void setGroupColumnIndex(Integer groupColumnIndex) {
		this.groupColumnIndex = groupColumnIndex;
	}

	public Integer getCufColumnIndex() {
		return cufColumnIndex;
	}

	public void setCufColumnIndex(Integer cufColumnIndex) {
		this.cufColumnIndex = cufColumnIndex;
	}

	public Integer getPhaseColumnIndex() {
		return phaseColumnIndex;
	}

	public void setPhaseColumnIndex(Integer phaseColumnIndex) {
		this.phaseColumnIndex = phaseColumnIndex;
	}

	public Integer getEffortsColumnIndex() {
		return effortsColumnIndex;
	}

	public void setEffortsColumnIndex(Integer effortsColumnIndex) {
		this.effortsColumnIndex = effortsColumnIndex;
	}

	public Integer getBlockerColumnIndex() {
		return blockerColumnIndex;
	}

	public void setBlockerColumnIndex(Integer blockerColumnIndex) {
		this.blockerColumnIndex = blockerColumnIndex;
	}

	public Integer getMajorColumnIndex() {
		return majorColumnIndex;
	}

	public void setMajorColumnIndex(Integer majorColumnIndex) {
		this.majorColumnIndex = majorColumnIndex;
	}

	public Integer getMinorColumnIndex() {
		return minorColumnIndex;
	}

	public void setMinorColumnIndex(Integer minorColumnIndex) {
		this.minorColumnIndex = minorColumnIndex;
	}

	public Integer getDdbdColumnIndex() {
		return ddbdColumnIndex;
	}

	public void setDdbdColumnIndex(Integer ddbdColumnIndex) {
		this.ddbdColumnIndex = ddbdColumnIndex;
	}

	public Integer getRecordUpdatedColumnIndex() {
		return recordUpdatedColumnIndex;
	}

	public void setRecordUpdatedColumnIndex(Integer recordUpdatedColumnIndex) {
		this.recordUpdatedColumnIndex = recordUpdatedColumnIndex;
	}

	@Override
	public String toString() {
		return "QualityDataColumnsDto [packColumnIndex=" + packColumnIndex + ", moduleColumnIndex=" + moduleColumnIndex
				+ ", groupColumnIndex=" + groupColumnIndex + ", cufColumnIndex=" + cufColumnIndex
				+ ", phaseColumnIndex=" + phaseColumnIndex + ", effortsColumnIndex=" + effortsColumnIndex
				+ ", blockerColumnIndex=" + blockerColumnIndex + ", majorColumnIndex=" + majorColumnIndex
				+ ", minorColumnIndex=" + minorColumnIndex + ", ddbdColumnIndex=" + ddbdColumnIndex
				+ ", recordUpdatedColumnIndex=" + recordUpdatedColumnIndex + "]";
	}

}
