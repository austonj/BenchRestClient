package com.auston;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.auston.TransactionRequest.HttpOperation;

public class TransactionHandler {

	private static final String BASE_ENDPOINT = "http://resttest.bench.co/transactions/";

	public TransactionHandler() {
		;
	}

	/**
	 * Create Transaction object(s) based on the passed in "response" parameter.
	 * The number of Transaction objects created will depend on the "response" parameter.
	 *
	 * @param response The JSON response.
	 * @return A list of Transaction objects.
	 */
	protected List<Transaction> createTransactionsFromJSON(String response) {

		JSONParser parser = new JSONParser();
		List<Transaction> transactionList = new ArrayList<Transaction>();

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
				if (currTransaction.containsKey("Amount") && currTransaction.containsKey("Date")) {
					amount = Double.parseDouble((String) currTransaction.get("Amount"));
					dateStr = (String) currTransaction.get("Date");
					Date date = Utilities.parseStringAsDate(dateStr);
					transactionList.add(new Transaction(date, amount));
				} else {
					System.out.println("Invalid transaction JSON object. Missing attribute(s)");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return transactionList;
	}

	/**
	 * Helper method for issuing HTTP GET requests to retrieve JSON response
	 * containing transaction information. This method will retry once for
	 * HTTP return code which is neither 200 or 404.
	 */
	private void handleTransactions() {

		boolean isDone = false;
		boolean hasRetried = false;
		int returnCode;
		int page = 1;

		while (!isDone) {
			TransactionRequest request = new TransactionRequest(
					BASE_ENDPOINT + String.valueOf(page++) + ".json", "");
			returnCode = request.executeRequest(HttpOperation.GET);
			if (returnCode == 200) {
				createTransactionsFromJSON(request.getResponse());
			} else if (returnCode == 404) {
				isDone = true;
			} else {
				if (!hasRetried) {
					hasRetried = true;
					returnCode = request.executeRequest(HttpOperation.GET);
				} else {
					isDone = true;
				}
			}
		}
	}

	public static void main(String[] args) {
		TransactionHandler handler = new TransactionHandler();
		handler.handleTransactions();
		Transaction.printDailyBalances();
		Transaction.printTotalBalance();
	}
}
