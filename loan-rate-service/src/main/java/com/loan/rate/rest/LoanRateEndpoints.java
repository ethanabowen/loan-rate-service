package com.loan.rate.rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Stopwatch;
import com.loan.rate.excel.domain.Report;
import com.loan.rate.excel.util.ExcelService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/LoanRates")
@Log4j
public class LoanRateEndpoints {

	@Autowired
	private ExcelService excelService;

	@RequestMapping(path = "/Process", method = RequestMethod.POST)
	public void processDailyRateFile() throws IOException {
		Stopwatch sw = Stopwatch.createStarted();
		
		log.info("Processing started...");
		excelService.processDailyRatesFile();
		log.info("Processing finished in " + sw.elapsed(TimeUnit.MILLISECONDS));
		sw.stop();
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public List<Report> getReportsForDate(@RequestParam("reportDate") String reportDate) throws IOException {
		log.info("Fetching reports for " + reportDate);
		return excelService.getReportsForDate(reportDate);
	}

}
