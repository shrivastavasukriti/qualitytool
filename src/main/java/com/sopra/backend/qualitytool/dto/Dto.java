package com.sopra.backend.qualitytool.dto;

import java.math.BigDecimal;
import java.util.Date;

public class Dto {

	private String externalTransactionID;
	private String clientId;
	private String securityId;
	private String transactionType;
	private Date transactionDate;
	private BigDecimal marketValue;
	private Boolean priority;
	private Double fees;

	public String getExternalTransactionID() {
		return externalTransactionID;
	}

	public void setExternalTransactionID(String externalTransactionID) {
		this.externalTransactionID = externalTransactionID;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(BigDecimal marketValue) {
		this.marketValue = marketValue;
	}

	public Boolean getPriority() {
		return priority;
	}

	public void setPriority(Boolean priority) {
		this.priority = priority;
	}

	public Double getFees() {
		return fees;
	}

	public void setFees(Double fees) {
		this.fees = fees;
	}
}
