package com.loan.rate.pdf.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Rebate {

	BigDecimal rebate;
	
	public Rebate(String rebate) {
		this.rebate = new BigDecimal(rebate);
	}
}