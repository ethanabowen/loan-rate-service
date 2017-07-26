package com.loan.rate.pdf.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LockDate {

	String date;
	Integer daysOut;

	public LockDate(Integer daysOut, String date) {
		this.date = date;
		this.daysOut = daysOut;
	}
	
	@Override
	public int hashCode() {
		return daysOut;
	}
}
