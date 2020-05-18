package com.sopra.backend.qualitytool.model.source;

public class EffortsDto {

	private String gcu;
	private Double sfd;
	private Double design;
	private Double code;
	private Double testPlan;
	private Double executeTest;

	public String getGcu() {
		return gcu;
	}

	public void setGcu(String gcu) {
		this.gcu = gcu;
	}

	public Double getSfd() {
		return sfd;
	}

	public void setSfd(Double sfd) {
		this.sfd = sfd;
	}

	public Double getDesign() {
		return design;
	}

	public void setDesign(Double design) {
		this.design = design;
	}

	public Double getCode() {
		return code;
	}

	public void setCode(Double code) {
		this.code = code;
	}

	public Double getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(Double testPlan) {
		this.testPlan = testPlan;
	}

	public Double getExecuteTest() {
		return executeTest;
	}

	public void setExecuteTest(Double executeTest) {
		this.executeTest = executeTest;
	}

	@Override
	public String toString() {
		return "EffortsDto [gcu=" + gcu + ", sfd=" + sfd + ", design=" + design + ", code=" + code + ", testPlan="
				+ testPlan + ", executeTest=" + executeTest + "]";
	}

}
