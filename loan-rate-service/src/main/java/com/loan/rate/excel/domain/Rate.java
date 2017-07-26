package com.loan.rate.excel.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Rate {

	BigDecimal rate;
	
	public Rate(BigDecimal rate) {
		this.rate = rate;
	}

	@Override
	public int hashCode() {
		return rate.intValue();
	}
}
