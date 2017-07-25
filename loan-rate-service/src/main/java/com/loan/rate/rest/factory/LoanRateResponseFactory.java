package com.loan.rate.rest.factory;

import org.springframework.stereotype.Component;

import com.loan.rate.domain.Country;
import com.loan.rate.rest.contract.LoanRateResponse;

@Component
public class LoanRateResponseFactory {

	public LoanRateResponse create(Country country) {
		LoanRateResponse response = new LoanRateResponse();
		
		response.setCountryName(country.getCountryName());
		response.setCountryUrl(country.getCountryUrl());
		response.setFlagImageLocation(country.getFlagImageLocation());
		
		return response;
	}
}
