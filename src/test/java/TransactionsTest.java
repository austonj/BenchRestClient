package test.java;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.auston.Transaction;
import com.auston.Utilities;

public class TransactionsTest extends Transaction {

	public TransactionsTest() {
		super(new Date(), 0);
	}

	@Test
	public void testUpdateBalanceByDate() {
		Transaction.clearTransactionByDateCache();
		Transaction i_transaction = new Transaction(Utilities.convertToDate("2010-10-10"), 100);
		assert(Transaction.getTransactionByDateCache().get(i_transaction.getDate()).getAmount() == 100.0);
	}

	@Test
	public void sortAscensindgTest() {
		Transaction.clearTransactionByDateCache();
		List<Transaction> transactionsList = new ArrayList<Transaction>();
		transactionsList.add(new Transaction(Utilities.convertToDate("2010-10-10"), 100));
		transactionsList.add(new Transaction(Utilities.convertToDate("2010-09-10"), 110));
		transactionsList.add(new Transaction(Utilities.convertToDate("2011-11-10"), 120));

		List<Date> verificationDateList = new ArrayList<Date>();
		Date date1 = Utilities.convertToDate("2010-09-10");
		Date date2 = Utilities.convertToDate("2010-10-10");
		Date date3 = Utilities.convertToDate("2011-11-10");
		verificationDateList.add(date1);
		verificationDateList.add(date2);
		verificationDateList.add(date3);

		// Sort in ascending order.
		Transaction.sortByDate(true);
		int index = 0;
		for (Date date : Transaction.getTransactionByDateCache().keySet()) {
			assert(date.compareTo(verificationDateList.get(index++)) == 0);
		}
	}

	@Test
	public void sortDescensindgTest() {
		Transaction.clearTransactionByDateCache();
		List<Transaction> transactionsList = new ArrayList<Transaction>();
		transactionsList.add(new Transaction(Utilities.convertToDate("2010-10-10"), 100));
		transactionsList.add(new Transaction(Utilities.convertToDate("2010-09-10"), 110));
		transactionsList.add(new Transaction(Utilities.convertToDate("2011-11-10"), 120));

		List<Date> verificationDateList = new ArrayList<Date>();
		Date date1 = Utilities.convertToDate("2011-11-10");
		Date date2 = Utilities.convertToDate("2010-10-10");
		Date date3 = Utilities.convertToDate("2010-09-10");

		verificationDateList.add(date1);
		verificationDateList.add(date2);
		verificationDateList.add(date3);

		// Sort in descending order.
		Transaction.sortByDate(false);
		int index = 0;
		for (Date date : Transaction.getTransactionByDateCache().keySet()) {
			assert(date.compareTo(verificationDateList.get(index++)) == 0);
		}
	}
}
