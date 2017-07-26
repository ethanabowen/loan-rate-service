package com.loan.rate.pdf.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Rate {

	BigDecimal rate;
	
	public Rate(String rate) {
		this.rate = new BigDecimal(rate);
	}

	@Override
	public int hashCode() {
		return rate.intValue();
	}
}
