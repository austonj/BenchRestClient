package com.auston;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransactionHandler {

	private AtomicBoolean i_isDone = new AtomicBoolean();
	private List<Future<Double>> i_transactionPagelist = Collections
			.synchronizedList(new ArrayList<Future<Double>>());
	private static final int NTHREDS = 10;
	private static final String BASE_ENDPOINT = "http://resttest.bench.co/transactions/";

	// Runnable thread for checking status of executing threads.
	private Runnable checkResponseCodeTask = () -> {

		// Check if any of the futures has returned -1.
		// -1 represents HTTP GET request failure from TransactionParser.
		while (!i_isDone.get()) {
			synchronized (i_transactionPagelist) {
				for (Iterator<Future<Double>> iterator = i_transactionPagelist.iterator(); iterator
						.hasNext();) {
					Future<Double> future = iterator.next();
					if (future.isDone()) {
						// TODO: future is skipped when done. Purpose?
						try {
							if (future.get() == Double.NEGATIVE_INFINITY) {
								this.i_isDone.set(true);
							} else {
								// TODO:
								iterator.remove();
							}
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	};

	private void retrieveTransactions() {

		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		int page = 1;
		while (!i_isDone.get()) {
			Callable<Double> worker = new TransactionClient(
					BASE_ENDPOINT + String.valueOf(page++) + ".json", "");
			Future<Double> submit = executor.submit(worker);
			i_transactionPagelist.add(submit);

			Thread checkResponses = new Thread(checkResponseCodeTask);
			checkResponses.start();
			// try {
			// Thread.sleep(10);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		try {
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
			TransactionCache.getInstance().printRemaining();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("Cancelling all executing thread");
			}
			executor.shutdownNow();
		}
	}

	public static void main(String[] args) {

		TransactionHandler client = new TransactionHandler();
		client.retrieveTransactions();
	}
}
