package com.auston;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestActions {
	// TODO: Structure interfaces, classes, helpers accordingly.
	// TODO: Correctly handle various HTTP responses. Retries? Necessary? 
	// TODO: Add helper for retrieving JSON objects.
	// TODO: Round results to 2 decimal places.
	// TODO: Add thread support per page for the possibility of many transaction pages.
	// TODO: Date queue? Necessary?
	
	public static void main(String[] args) {
		String response = httpGet("http://resttest.bench.co/transactions/1.json", "");
//		System.out.println(response);

		Double amountTot = 0.0;
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(response);
			JSONObject jsonObject = (JSONObject) obj;

			// loop array
			JSONArray msg = (JSONArray) jsonObject.get("transactions");
			String prevDate = null;
			for (int i=0; i<msg.size(); i++) {				
				// Get transaction amount.
				JSONObject amountObj = (JSONObject) msg.get(i);				
				double amountVal = Double.parseDouble((String)amountObj.get("Amount"));
				
				// Get date.							
				String date = (String)amountObj.get("Date");
				
				// Check if current and prev transaction as on the same day.
				if (prevDate == null)
					prevDate = date;
				if (!prevDate.equals(date)) {
					System.out.println(prevDate + ": " + amountTot);
				}
				prevDate = date;
				
				// Add transaction to total balance.
				amountTot += amountVal;
			}
			System.out.println(prevDate + ": " + amountTot);
			System.out.println("Total: " + amountTot);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static String httpGet(String url, String urlParameters) {
		try {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
//		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return "Nothing";
	}
	
	public static String POST(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
