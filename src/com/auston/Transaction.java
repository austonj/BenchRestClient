package com.auston;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Transaction {
	private Date i_date;
	private double i_amount;
	private static double s_totalBalance;
	private static LinkedHashMap<Date, Transaction> s_transactionByDateCache =
			new LinkedHashMap<Date, Transaction>();

	public Transaction(Date date, double amount) {
		i_date = date;
		i_amount = amount;
		updateTransactionByDateCache();
	}

	public static void printTotalBalance() {
		System.out.println("Total: " + new DecimalFormat("##.00").format(s_totalBalance));
	}

	public static LinkedHashMap<Date, Transaction> getTransactionByDateCache() {
		return s_transactionByDateCache;
	}

	public static void clearTransactionByDateCache() {
		s_transactionByDateCache.clear();
	}

	public static void printDailyBalances() {
		double dailyBalance = 0;
		sortByDate(true);
		synchronized (s_transactionByDateCache) {
			for (Entry<Date, Transaction> entry : s_transactionByDateCache.entrySet()) {
				dailyBalance += entry.getValue().getAmount();
				System.out.println(entry.getKey() + ": " + new DecimalFormat("##.00").format(dailyBalance));
			}
		}
	}

	public Date getDate() {
		return i_date;
	}

	public double getAmount() {
		return i_amount;
	}

	public void setDate(Date date) {
		i_date = date;
	}

	public void setAmount(double amount) {
		i_amount = amount;
	}

	public static double getTotalBalance() {
		return s_totalBalance;
	}

	public void clearTotalBalance() {
		s_totalBalance = 0;
	}

	protected void updateTransactionByDateCache() {

		// Update total balance.
		s_totalBalance += i_amount;

		// If no entry exists for transaction.getDate(), add a new entry to the map.
		// If an entry already exists, update it's amount with the current transaction object's amount.
		Date date = this.getDate();
		synchronized (s_transactionByDateCache) {
			if (s_transactionByDateCache.putIfAbsent(date, this) != null) {
				double existingAmount = s_transactionByDateCache.get(date).getAmount();
				s_transactionByDateCache.get(date).setAmount(existingAmount + i_amount);
			}
		}
	}

	protected static void sortByDate(boolean ascending) {
		synchronized (s_transactionByDateCache) {
			List<Map.Entry<Date, Transaction>> entries = new ArrayList<Map.Entry<Date, Transaction>>(
					s_transactionByDateCache.entrySet());

			if (ascending) {
				Collections.sort(entries,
						(Entry<Date, Transaction> a, Entry<Date, Transaction> b) -> a.getKey().compareTo(b.getKey()));
			} else {
				Collections.sort(entries, Collections.reverseOrder(
						(Entry<Date, Transaction> a, Entry<Date, Transaction> b) -> a.getKey().compareTo(b.getKey())));
			}
			s_transactionByDateCache.clear();
			for (Map.Entry<Date, Transaction> e : entries) {
				s_transactionByDateCache.put(e.getKey(), e.getValue());
			}
		}
	}
}
