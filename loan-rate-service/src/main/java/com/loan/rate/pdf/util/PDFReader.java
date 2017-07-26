package com.loan.rate.pdf.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.loan.rate.domain.factory.CaliberRateFactory;
import com.loan.rate.pdf.domain.CaliberRateTable;

public class PDFReader {

	private static final String FILE_NAME = "C:\\Users\\Ethan\\workspace\\country-article-service\\src\\main\\java\\com\\wh\\random\\Caliber Rate Sheet.pdf";
	private static final String NEW_FILE_NAME = "C:\\Users\\Ethan\\workspace\\country-article-service\\src\\main\\java\\com\\wh\\random\\Caliber Rate Sheet TEST.txt";

	public static void main(String[] args) {

		// PdfReader reader;
		//
		// try {
		//
		// reader = new PdfReader(FILE_NAME);
		//
		// // pageNumber = 1
		// String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);
		// //System.out.println(textFromPage);
		//
		//
		// reader.close();
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		System.out.println(
				"*********************************************************************************************************************************************************************");
		PDFManager pdfManager = new PDFManager();
		pdfManager.setFilePath(FILE_NAME);

		try {
			PDDocument pdDoc = pdfManager.getPDDocumentFromPDFFile();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			CaliberRateFactory caliberRateFactory = new CaliberRateFactory();
			File file = new File(NEW_FILE_NAME);
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(NEW_FILE_NAME));

			for (int i = 1; i <= pdDoc.getNumberOfPages(); i++) {
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
				String pdfText = pdfStripper.getText(pdDoc);

				List<CaliberRateTable> caliberRateTables = caliberRateFactory.build(pdfText);
				for (CaliberRateTable table : caliberRateTables) {
					try {
						System.out.println(table.prettyPrintRates());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				bw.append(pdfText);

			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}