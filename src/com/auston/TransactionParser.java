package com.auston;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Callable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TransactionParser implements Callable<Double> {

	private String i_response;
	private String i_url;
	private String i_urlParameters;

	public TransactionParser(String url, String urlParameters) {
		this.i_url = url;
		this.i_urlParameters = urlParameters;
	}

	private int getTransactions(String url, String urlParameters) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				i_response = response.toString();
			}

			return responseCode;
		} catch (IOException e) {
			e.printStackTrace();
			return 400;
		}
	}

	private double parseTransactionsJSON(String response) {
		double totBalance = 0.0;
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
				TransactionCache.getTransactionsInfo().put(date, amount);
				// Add current transaction amount to total balance.
				TransactionCache.getTransactionsInfo().updateTotalBalance(amount);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return totBalance;
	}

	@Override
	public Double call() throws Exception {
		// Execute HTTP GET request.
		int responseCode = getTransactions(this.i_url, this.i_urlParameters);
		// TODO: what if it's 400?
		if (responseCode != 200) {
			return Double.NEGATIVE_INFINITY;
		}
		return parseTransactionsJSON(i_response);
	}
}
