package com.loan.rate.rest.contract;

import java.util.List;

import lombok.Data;

@Data
public class LoanRatesResponse {
	private List<LoanRateResponse> countriesResponse;
}
