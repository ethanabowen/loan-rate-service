package com.loan.rate.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Table;
import com.loan.rate.excel.domain.Rate;
import com.loan.rate.excel.domain.Rebate;
import com.loan.rate.excel.util.ExcelService;

@RestController
public class LoanRateEndpoints {
	
	@Autowired
	private ExcelService excelService;

	@RequestMapping(value="/LoanRates/", method=RequestMethod.GET)
	public List<Table<String, Rate, List<Rebate>>>  processDailyRateFile() throws IOException {

		return excelService.processDailyRatesFile(); 
	}

}
