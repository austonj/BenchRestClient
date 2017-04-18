package test.java;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.auston.Transaction;
import com.auston.TransactionHandler;
import com.auston.Utilities;

public class TransactionHandlerTest extends TransactionHandler {

	private static final String s_response = "{\n    \"totalCount\": 38,\n    \"page\": 4,\n    "
			+ "\"transactions\": [{\n        \"Date\": \"2013-12-12\",\n        "
			+ "\"Ledger\": \"Insurance Expense\",\n        \"Amount\": \"-117.81\",\n        "
			+ "\"Company\": \"LONDON DRUGS 78 POSTAL VANCOUVER BC\"\n    }, {\n        "
			+ "\"Date\": \"2013-12-15\",\n        \"Ledger\": \"Equipment Expense\",\n        "
			+ "\"Amount\": \"-520.85\",\n        "
			+ "\"Company\": \"ECHOSIGN xxxxxxxx6744 CA xx8.80 USD @ xx0878\"\n    }]}";

	public TransactionHandlerTest() {

	}

	@Override
	protected List<Transaction> createTransactionsFromJSON(String response) {
		return super.createTransactionsFromJSON(response);
	}

	@Test
	public void testCreateTransactionSuccess() {
		List<Date> verificationDateList = new ArrayList<Date>();
		List<Double> verificationAmountList = new ArrayList<Double>();
		Date date1 = Utilities.parseStringAsDate("2013-12-12");
		Date date2 = Utilities.parseStringAsDate("2013-12-15");
		double amount1 = -117.81;
		double amount2 = -520.85;

		verificationDateList.add(date1);
		verificationDateList.add(date2);
		verificationAmountList.add(amount1);
		verificationAmountList.add(amount2);

		List<Transaction> transactions = createTransactionsFromJSON(s_response);
		for (int i=0; i<transactions.size(); i++) {
			assert(transactions.get(i).getDate().compareTo(verificationDateList.get(i)) == 0);
			assert(transactions.get(i).getAmount() == verificationAmountList.get(i));
		}
	}
}
