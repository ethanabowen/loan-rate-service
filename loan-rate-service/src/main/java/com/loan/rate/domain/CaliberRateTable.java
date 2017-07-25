package com.loan.rate.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CaliberRateTable {

	private String tableRateType;
	private Map<Rate, Map<LockDate, Rebate>> rates = new HashMap<>();

	public String prettyPrintRates() {
		StringBuilder prettyPrint = new StringBuilder();
		prettyPrint.append(tableRateType + "\n");

		List<Rate> ratesKeys = new ArrayList<>(rates.keySet());
		// get lock dates from first rate for printing
		List<LockDate> lockDates = new ArrayList<>(rates.get(ratesKeys.get(0)).keySet());

		// print locks dates
		prettyPrint.append("\t  ");
		for (LockDate lockDate : lockDates) {
			prettyPrint.append(lockDate.getDate() + "\t");
		}
		prettyPrint.append("\n\t\t");

		// print locks dates
		for (LockDate lockDate : lockDates) {
			prettyPrint.append("   " + lockDate.getDaysOut() + "\t");
		}
		prettyPrint.append("\n");

		// print rates and rebates
		for(Rate rate: ratesKeys) {
			List<Rebate> rebates = new ArrayList<>(rates.get(rate).values());
			
			prettyPrint.append(rate.getRate() + "\t");
			for(Rebate rebate: rebates) {
				prettyPrint.append(rebate.getRebate() + "\t");
			}
			
			prettyPrint.append("\n");
		}
		return prettyPrint.toString();
	}

}
