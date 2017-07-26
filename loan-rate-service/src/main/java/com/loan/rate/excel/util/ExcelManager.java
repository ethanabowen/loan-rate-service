package com.loan.rate.excel.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.loan.rate.excel.domain.LockDate;
import com.loan.rate.excel.domain.Rate;
import com.loan.rate.excel.domain.Rebate;


public class ExcelManager {
	private static final String FILE_NAME = "C:\\Users\\Ethan\\git\\loan-rate-service\\loan-rate-service\\src\\main\\java\\com\\loan\\rate\\excel\\util\\RateSheet-7710.csv";

	public static void main(String[] args) {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		Table<String, Rate, List<Rebate>> table = HashBasedTable.create();
		try {

			br = new BufferedReader(new FileReader(FILE_NAME));

			List<LockDate> lockDates = null;
			while ((line = br.readLine()) != null) {

				// use comma as separator
				List<String> lineValues = Arrays.asList(line.split(cvsSplitBy));
				
//				System.out.println(lineValues);
				if (lineValues != null && lineValues.size() > 1) {
					if(lineValues.get(0).toLowerCase().equals("ratesheet name")) {
						lockDates = new ArrayList<>();
						for (int i = 3; i < lineValues.size(); i++) {
							LockDate lockDate = new LockDate();
							
							String dayStr = lineValues.get(i);
							lockDate.setDaysOut(Integer.parseInt(dayStr.substring(0,dayStr.indexOf(" "))));
							lockDate.setDate(null);
							
							lockDates.add(lockDate);
						}
						continue;
					}
					
					
					String rateTableName = lineValues.get(1);
					Rate rate= new Rate(new BigDecimal(lineValues.get(2)));
					List<Rebate> rebates = new ArrayList<>();
					Date dateOfRate = new Date();

					for (int i = 3; i < lineValues.size(); i++) {
						
						Rebate rebate = new Rebate();
						if(lineValues.get(i).trim().length() > 0) {
							rebate.setRebate(new BigDecimal(lineValues.get(i)));
						} else {
							rebate.setRebate(null);
						}
						rebate.setLockDate(lockDates.get(i-3));
						rebates.add(rebate);
					}
					table.put(rateTableName, rate, rebates);

//					System.out.println(line);

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(prettyPrintTable(table));
	}

	private static String prettyPrintTable(Table<String, Rate, List<Rebate>> table) {
		StringBuilder prettyPrint = new StringBuilder();
		
		
		
		for(String tableKey : table.rowKeySet()) {
		
			// table name
			prettyPrint.append(tableKey + "\n");
			
			// lock dates
			List<LockDate> lockDates = new ArrayList<>();
			List<Rebate> lockDatesHeader = table.values().iterator().next();
			
			for(Rebate rebate: lockDatesHeader) {
				lockDates.add(rebate.getLockDate());
			}
			prettyPrint.append("\t");
			for(LockDate lockDate : lockDates) {
				prettyPrint.append(lockDate.getDaysOut() + "\t");
			}
			prettyPrint.append("\n");
			
			// rates and rebates
			Map<Rate, List<Rebate>> ratesWithRebateValues = table.row(tableKey);
			for(Rate rateKey : ratesWithRebateValues.keySet()) {
				prettyPrint.append(rateKey.getRate() + "\t");
				
				List<Rebate> rebates = ratesWithRebateValues.get(rateKey);
				for(Rebate rebate: rebates) {
					if(rebate.getRebate() != null) {
						prettyPrint.append(rebate.getRebate() + "\t");
					} else {
						prettyPrint.append("\t");
					}
				}
				prettyPrint.append("\n");
			}
		}
		
		return prettyPrint.toString();
		
	}
};