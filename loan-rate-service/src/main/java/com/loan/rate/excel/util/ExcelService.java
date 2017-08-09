package com.loan.rate.excel.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.loan.rate.excel.domain.LockDate;
import com.loan.rate.excel.domain.Rate;
import com.loan.rate.excel.domain.Rebate;
import com.loan.rate.excel.domain.Report;

@Component
public class ExcelService {
	private static final String FOLDER_NAME = "C:\\Users\\Ethan\\git\\loan-rate-service\\loan-rate-service\\src\\main\\resources\\";

	private Map<String, List<Report>> reportsByDate;

	public void processDailyRatesFile() throws IOException {

		List<Path> files = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(Paths.get(FOLDER_NAME))) {
			paths.filter(p -> p.toString().indexOf(".csv") > 0).forEach(p -> files.add(p.toAbsolutePath()));
		}

		reportsByDate = new HashMap<>();

		for (Path file : files) {
			BufferedReader br = new BufferedReader(new FileReader(FOLDER_NAME + file.getFileName()));
			String line = "";
			String CSV_DELIMITER = ",";
			try {

				String dateLine = br.readLine();
				int dateIndex = dateLine.indexOf(": ") + 2;
				String rateSheetDate = dateLine.substring(dateIndex, dateIndex + 10);
				
				/*Date rateSheetDate = null;
				if (dateLine != null && dateLine.length() > 1) {
					int dateIndex = dateLine.indexOf(": ") + 2;
					rateSheetDate = new Date(dateLine.substring(dateIndex, dateIndex + 10));
				}*/
				
				List<LockDate> lockDates = null;
				List<String[]> reportLines = new ArrayList<>();
				while ((line = br.readLine()) != null) {

					// use comma as separator
					List<String> headerLineValues = Arrays.asList(line.split(CSV_DELIMITER));

					if (headerLineValues != null && headerLineValues.size() > 1) {
						// parse Lock Date values (1 time)
						if (headerLineValues.get(0).toLowerCase().equals("ratesheet name")) {
							lockDates = new ArrayList<>();
							for (int i = 3; i < headerLineValues.size(); i++) {
								LockDate lockDate = new LockDate();
								String dayStr = headerLineValues.get(i);
								lockDate.setDaysOut(Integer.parseInt(dayStr.substring(0, dayStr.indexOf(" "))));

								Calendar cal = Calendar.getInstance();
								// cal.setTime(Calendar.getInstance().getTime());
								cal.add(Calendar.DATE, lockDate.getDaysOut());
								lockDate.setDate(cal.getTime().toString());

								lockDates.add(lockDate);
							}
							continue;
						}

						int nameIndex = headerLineValues.get(1).indexOf("(");
						
						Report report = new Report();
						if(reportsByDate.containsKey(rateSheetDate)) {
							report.setId(new Long(reportsByDate.get(rateSheetDate).size() + 1));	
						} else {
							report.setId(new Long(1));
						}
						
						report.setReportName(headerLineValues.get(1).substring(0, nameIndex-1));
						
						int abbrevIndex = headerLineValues.get(1).indexOf("(");
						String abbrev = headerLineValues.get(1).substring(abbrevIndex+1).replace(")", "");
						report.setReportAbbrev(abbrev);
						
						// have to cover the first line read
						reportLines.add(line.split(CSV_DELIMITER));

						while ((line = br.readLine()) != null && line.contains((report.getReportName()))) {
							reportLines.add(line.split(CSV_DELIMITER));
						}

						Map<LockDate, Map<Rate, Rebate>> reportValues = parseReportValuesFromCSV(lockDates,
								reportLines);
						report.setValues(reportValues);

						if (!reportsByDate.containsKey(rateSheetDate)) {
							List<Report> reportList = new ArrayList<>();
							reportList.add(report);
							reportsByDate.put(rateSheetDate, reportList);
						} else {
							reportsByDate.get(rateSheetDate).add(report);
						}

						reportLines.clear();
						// have to cover last line read
						if(line != null) {
							reportLines.add(line.split(CSV_DELIMITER));
						}
					}
				}
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
		}
	}

	private Map<LockDate, Map<Rate, Rebate>> parseReportValuesFromCSV(List<LockDate> lockDates,
			List<String[]> reportLines) {

		Map<LockDate, Map<Rate, Rebate>> report = new HashMap<LockDate, Map<Rate, Rebate>>();

		//@formatter:off
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 2.750, 0.662, 0.740, 0.818, 0.901, , 1.276],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 2.875, 0.076, 0.154, 0.232, 0.315, , 0.690],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 2.990, -0.397, -0.319, -0.241, -0.157, , 0.218],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 3.000, -0.448, -0.369, -0.291, -0.208, , 0.167],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 3.125, -0.908, -0.845, -0.767, -0.684, , -0.309],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 3.250, -1.531, -1.469, -1.406, -1.339, , -0.964],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 3.375, -2.008, -1.945, -1.883, -1.816, , -1.441],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 3.500, -2.487, -2.437, -2.374, -2.308, , -1.933],
		// [Dallas Wholesale - Borrower Paid, Conforming Fixed 15 (CF15), 4.125, -4.269, -4.219, -4.157, -4.090, , -3.715],
		//@formatter:on

		for (String[] reportLine : reportLines) {

			Rate rate = new Rate();
			for (int i = 2; i < reportLine.length; i++) {
				if (i == 2) {
					rate.setRate(new BigDecimal(reportLine[i]));
					continue;
				}

				Rebate rebate = new Rebate();
				String rebateStr = reportLine[i];
				if (rebateStr.trim().length() > 0) {
					rebate.setRebate(new BigDecimal(rebateStr));
				} else {
					rebate.setRebate(null);
				}

				if (!report.containsKey(lockDates.get(i - 3))) {
					Map<Rate, Rebate> rateRebate = new HashMap<>();
					rateRebate.put(rate, rebate);
					report.put(lockDates.get(i - 3), rateRebate);
				} else {
					report.get(lockDates.get(i - 3)).put(rate, rebate);
				}
			}

		}
		return report;
	}

	public List<Report> getReportsForDate(String reportDate) {
		return reportsByDate.get(reportDate);
	}
};