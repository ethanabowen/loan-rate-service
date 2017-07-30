package com.loan.rate.excel.domain;

import lombok.Data;

@Data
public class Report {

	private Long id;
	private String reportName;
	private String reportAbbrev;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportAbbrev() {
		return reportAbbrev;
	}
	public void setReportAbbrev(String reportAbbrev) {
		this.reportAbbrev = reportAbbrev;
	}
	
	
}
