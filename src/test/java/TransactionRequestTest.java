package test.java;

import org.junit.Test;

import com.auston.TransactionRequest;

public class TransactionRequestTest extends TransactionRequest {

	public TransactionRequestTest() {
		super("http://resttest.bench.co/transactions/1.json", "");
	}

	@Override
	protected short GET() {
		return super.GET();
	}

	@Test
	public void testGET() {
		assert(GET() == 200);
	}
}
