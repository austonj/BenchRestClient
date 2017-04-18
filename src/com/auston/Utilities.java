package com.auston;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

	public static Date convertToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readInputStream(InputStream input) throws IOException {
		String inputLine;
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
}
