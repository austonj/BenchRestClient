package junit;

import org.junit.Test;

import com.auston.TransactionClient;

public class TransactionClientTests extends TransactionClient {

	private static final String i_response = "{\n    \"totalCount\": 38,\n    \"page\": 4,\n    "
			+ "\"transactions\": [{\n        \"Date\": \"2013-12-13\",\n        "
			+ "\"Ledger\": \"Insurance Expense\",\n        \"Amount\": \"-117.81\",\n        "
			+ "\"Company\": \"LONDON DRUGS 78 POSTAL VANCOUVER BC\"\n    }, {\n        "
			+ "\"Date\": \"2013-12-13\",\n        \"Ledger\": \"Equipment Expense\",\n        "
			+ "\"Amount\": \"-520.85\",\n        "
			+ "\"Company\": \"ECHOSIGN xxxxxxxx6744 CA xx8.80 USD @ xx0878\"\n    }]}";

	public TransactionClientTests() {
		super("http://resttest.bench.co/transactions/1.json", "");

	}

	@Override
	protected int getTransactions(String url, String urlParameters) {
		return super.getTransactions(url, urlParameters);
	}

	@Test
	public void getTransactionsTest() {
		int code = getTransactions("http://resttest.bench.co/transactions/1.json", "");
		assert (code == 200);
	}
}
