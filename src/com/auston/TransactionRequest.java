package com.auston;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransactionRequest {

	public enum HttpOperation {
		GET, HEAD, POST, PUT
	}

	private String i_response = "NOT_YET_EXECUTED";
	private short i_returnCode;
	private String i_url;
	private String i_urlParameters;

	public TransactionRequest(String url, String urlParameters) {
		i_url = url;
		i_urlParameters = urlParameters;
	}

	public short executeRequest(HttpOperation op) {
		if (op == HttpOperation.GET) {
			return GET();
		}
		if (op == HttpOperation.POST) {
			// TODO: Implement.
		}
		if (op == HttpOperation.HEAD) {
			// TODO: Implement.
		}
		if (op == HttpOperation.PUT) {
			// TODO: Implement.
		}
		return -1;
	}

	/**
	 * HTTP GET request.
	 *
	 * @return HTTP return code.
	 */
	protected short GET() {
		try {
			// Send HTTP GET request.
			URL obj = new URL(i_url + "?" + i_urlParameters);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// Retrieve response.
			i_returnCode = (short) con.getResponseCode();
			if (i_returnCode == 200) {
				i_response = Utilities.readInputStream(con.getInputStream());
			}

			return i_returnCode;
		} catch (IOException e) {
			return -1;
		}
	}

	public short getReturnCode() {
		return i_returnCode;
	}

	public String getResponse() {
		return i_response;
	}
}
