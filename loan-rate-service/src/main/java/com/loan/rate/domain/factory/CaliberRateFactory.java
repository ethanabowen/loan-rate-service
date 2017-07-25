package com.loan.rate.domain.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.loan.rate.domain.CaliberRateTable;
import com.loan.rate.domain.LockDate;
import com.loan.rate.domain.Rate;
import com.loan.rate.domain.Rebate;

public class CaliberRateFactory {

	public List<CaliberRateTable> build(String pdfText) {
		List<CaliberRateTable> caliberRateTables = new ArrayList<>();

		List<String> lines = Arrays.asList(pdfText.split("\n"));
		Iterator<String> iter = lines.iterator();

		while (iter.hasNext()) {
			String line = iter.next();
			if (isStartOfRateTable(line.split(" ")[0])) {//ex. = CF15
				String tableRateTypeAndFirstLockDate = iter.next();
				String tableRateType = tableRateTypeAndFirstLockDate.split(" ")[0];

				// Lock Dates
				List<LockDate> lockDates = new ArrayList<>();
				lockDates.add(buildFirstLockDate(iter, tableRateTypeAndFirstLockDate));
				lockDates.addAll(buildLockDates(iter));

				// Rates
				CaliberRateTable caliberRateTable = new CaliberRateTable();
				caliberRateTable.setTableRateType(tableRateType);

				String rateLine;
				while ((rateLine = iter.next()).indexOf("%") == 5) {
					List<String> ratesAndRebates = Arrays.asList(rateLine.split(" "));
					Rate rate = new Rate(ratesAndRebates.get(0).replace("%", "").trim());
					//ratesAndRebates.remove(0); --can't be used because Arrays.asList returns a stupid fixed size

					// Assign the Rebates to LockDates
					Map<LockDate, Rebate> rebatesByLockDate = new HashMap<>();
					for (int i = 0; i < lockDates.size(); i++) {
						Rebate rebate = new Rebate(ratesAndRebates.get(i + 1).replaceAll("[()]", "").trim());//offset rate value
						rebatesByLockDate.put(lockDates.get(i), rebate);
					}

					caliberRateTable.getRates().put(rate, rebatesByLockDate);
				}
				
				caliberRateTables.add(caliberRateTable);
			}

		}
		return caliberRateTables;
	}

	private LockDate buildFirstLockDate(Iterator<String> iter, String firstLockDate) {
		LockDate lockDate = new LockDate();

		int startDaysOutIndex = firstLockDate.indexOf(" ") + 1;
		int endDaysOutIndex = firstLockDate.lastIndexOf(" ");
		lockDate.setDaysOut(Integer.parseInt(firstLockDate.substring(startDaysOutIndex, endDaysOutIndex)));
		lockDate.setDate(iter.next().trim());

		return lockDate;
	}

	private List<LockDate> buildLockDates(Iterator<String> iter) {
		List<LockDate> lockDates = new ArrayList<>();
		final Integer FUTURE_LOCK_DATE_COUNT = 3;
		for (int i = 0; i < FUTURE_LOCK_DATE_COUNT; i++) {

			String futureLockDate = iter.next();
			futureLockDate = futureLockDate.substring(0, futureLockDate.indexOf(" "));
			LockDate lockDate = new LockDate(Integer.parseInt(futureLockDate), iter.next().trim());

			lockDates.add(lockDate);
		}

		return lockDates;
	}

	private boolean isStartOfRateTable(String line) {
		return line.matches("\\d\\d\\(\\d\\d-\\d\\d\\)");
	}
}
