package com.loan.rate.excel.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Report {

	private Long id;
	private String reportName;
	private String reportAbbrev;
	private Map<Integer, List<RatesAndRebates>> values;
	
}
