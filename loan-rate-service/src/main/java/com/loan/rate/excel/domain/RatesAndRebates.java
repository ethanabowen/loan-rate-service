package com.loan.rate.excel.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RatesAndRebates {
	
	private BigDecimal rate;
	private BigDecimal rebate;
	
}
