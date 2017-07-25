package com.loan.rate.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loan.rate.rest.contract.LoanRatesResponse;
import com.loan.rate.service.CountryService;

@RestController
public class LoanRateEndpoints {
	
	@Autowired
	private CountryService countryService;

	@RequestMapping(value="/Countries/", method=RequestMethod.GET)
	public LoanRatesResponse getCountrys() {
		LoanRatesResponse countries = new LoanRatesResponse();
		
		countries.setCountriesResponse(countryService.getCountries());
		
		return countries; 
	}

}
