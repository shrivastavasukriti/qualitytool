package com.sopra.backend.qualitytool.model.source;

import java.util.Date;

public class QualityDataDto {

	private String pack;
	private String module;
	private String group;
	private String phase;
	private Double effort;
	private Integer blocker;
	private Integer major;
	private Integer minor;
	private Date dateRecordUpdate;

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}
	
	public Double getEffort() {
		return effort;
	}

	public void setEffort(Double effort) {
		this.effort = effort;
	}

	public Integer getBlocker() {
		return blocker;
	}

	public void setBlocker(Integer blocker) {
		this.blocker = blocker;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	public Date getDateRecordUpdate() {
		return dateRecordUpdate;
	}

	public void setDateRecordUpdate(Date dateRecordUpdate) {
		this.dateRecordUpdate = dateRecordUpdate;
	}

	@Override
	public String toString() {
		return "QualityDataDto [pack=" + pack + ", module=" + module + ", group=" + group + ", phase=" + phase
				+ ", effort=" + effort + ", blocker=" + blocker + ", major=" + major + ", minor=" + minor
				+ ", dateRecordUpdate=" + dateRecordUpdate + "]";
	}

	
}
