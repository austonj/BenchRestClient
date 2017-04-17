package com.auston;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utilities {

	public static Date convertToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void parseTransactionsJSON(String response) {
		JSONParser parser = new JSONParser();

		try {
			// Parse HTTP response into JSON objects.
			JSONObject jsonObject = (JSONObject) parser.parse(response);
			// Obtain list of transactions for the current page.
			JSONArray transactions = (JSONArray) jsonObject.get("transactions");

			// Loop through the list of transaction.
			double amount = 0.0;
			String dateStr = null;
			for (int i = 0; i < transactions.size(); i++) {
				JSONObject currTransaction = (JSONObject) transactions.get(i);
				amount = Double.parseDouble((String) currTransaction.get("Amount"));
				dateStr = (String) currTransaction.get("Date");
				Date date = Utilities.convertToDate(dateStr);

				// Add current transaction to TransactionCache.cache.
				TransactionCache.getInstance().put(date, amount);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
