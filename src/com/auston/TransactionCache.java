package com.auston;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TransactionCache {
	// TODO: get number of cores. RunTime.Runtime available processors.
	private static final int CACHE_SIZE = 20;
	private static TransactionCache transactionInfo = null;
	private double totalBalance = 0.0;
	private double dailyBalance = 0.0;
	private Date prevDate = null;

	private final Map<Date, Double> cache = new LinkedHashMap<Date, Double>(CACHE_SIZE,
			(float) 0.75, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected synchronized boolean removeEldestEntry(Map.Entry<Date, Double> eldest) {
			if (size() > CACHE_SIZE) {
				Date firstKey = cache.keySet().iterator().next();
				dailyBalanceCache.put(firstKey, cache.get(firstKey));
				// sort(dailyBalanceCache, true);
				cache.remove(firstKey);
			}
			return false;
		}
	};

	private final Map<Date, Double> dailyBalanceCache = new LinkedHashMap<Date, Double>(CACHE_SIZE,
			(float) 0.75, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected synchronized boolean removeEldestEntry(Map.Entry<Date, Double> eldest) {
			Date firstKey = dailyBalanceCache.keySet().iterator().next();
			// Remove the first element and print out the previous date's daily
			// balance. The first element should be the oldest date in the
			// cache.
			if (size() > CACHE_SIZE) {
				if (prevDate == null) {
					prevDate = firstKey;
				}
				dailyBalance += dailyBalanceCache.get(firstKey);
				System.out.println(prevDate + ": " + dailyBalance);
				dailyBalanceCache.remove(firstKey);
			}
			return false;
		}
	};

	private TransactionCache() {
		;
	}

	public synchronized static TransactionCache getInstance() {
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
			updateTotalBalance(amount);
		}
	}

	public void printRemaining() {
		synchronized (cache) {
			// sort(cache, true);
			for (Entry<Date, Double> entry : cache.entrySet()) {
				Date key = entry.getKey();
				if (prevDate == null) {
					prevDate = key;
					dailyBalance += entry.getValue();
				}
				dailyBalance += entry.getValue();
				System.out.println(prevDate + ": " + dailyBalance);
				prevDate = key;
				// System.out.println(key + ": " + entry.getValue());
			}
		}
		System.out.println("Total: " + totalBalance);
	}

	public void sort(Map<Date, Double> map, boolean ascending) {
		synchronized (map) {
			List<Map.Entry<Date, Double>> entries = new ArrayList<Map.Entry<Date, Double>>(
					map.entrySet());

			if (ascending) {
				Collections.sort(entries, (Entry<Date, Double> a, Entry<Date, Double> b) -> a
						.getKey().compareTo(b.getKey()));
			} else {
				Collections.sort(entries, Collections.reverseOrder((Entry<Date, Double> a,
						Entry<Date, Double> b) -> a.getKey().compareTo(b.getKey())));
			}

			map.clear();
			for (Map.Entry<Date, Double> e : entries) {
				map.put(e.getKey(), e.getValue());
			}
		}
	}

	public synchronized void updateTotalBalance(double update) {
		totalBalance += update;
	}
}
