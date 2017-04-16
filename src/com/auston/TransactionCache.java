package com.auston;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TransactionCache {
	private static final int DATE_MAP_SIZE = 20;
	private static TransactionCache transactionInfo = null;
	private final Map<Date, Double> cache = new LinkedHashMap<Date, Double>(DATE_MAP_SIZE,
			(float) 0.75, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<Date, Double> eldest) {
			return size() > DATE_MAP_SIZE;
		}
	};
	private double totalBalance = 0.0;

	public TransactionCache() {
		;
	}

	public synchronized static TransactionCache getTransactionsInfo() {
		if (transactionInfo == null) {
			transactionInfo = new TransactionCache();
		}
		return transactionInfo;
	}

	public void put(Date date, double amount) {
		synchronized (cache) {
			if (cache.putIfAbsent(date, amount) != null) {
				cache.put(date, cache.get(date) + amount);
			}
		}
	}

	public void printRemaining() {
		synchronized (cache) {
			for (Entry<Date, Double> entry : cache.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		}
		System.out.println("Total: " + totalBalance);
	}

	public synchronized void updateTotalBalance(double update) {
		totalBalance += update;
	}
}
