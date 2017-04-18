package test.java;

import org.junit.Test;

import com.auston.TransactionRequest;

public class TransactionRequestTest extends TransactionRequest {

	public TransactionRequestTest() {
		super("Placeholder", "");
	}

	@Override
	protected short GET() {
		return super.GET();
	}

	@Test
	public void testGETSuccess() {
		TransactionRequest request =
				new TransactionRequest("http://resttest.bench.co/transactions/1.json", "");
		assert(request.executeRequest(HttpOperation.GET) == 200);
	}

	@Test
	public void testGETFailure() {
		TransactionRequest request =
				new TransactionRequest("Invalid request", "");
		assert(request.executeRequest(HttpOperation.GET) != 200);
	}
}
