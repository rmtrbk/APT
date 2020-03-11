package au.com.ap.aptouch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import au.com.ap.aptouch.service.TransactionAuditServiceImpl;

/**
 * Entry point to the execution
 * 
 * @author Theja Kotuwella
 *
 */
@SpringBootApplication
public class APTouchApplication implements CommandLineRunner {
	@Autowired
	TransactionAuditServiceImpl transactionAuditService;

	public static void main(String[] args) {
		SpringApplication.run(APTouchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n########### AP Touch Application ###########");
		
		runSimulation();
		
		System.out.println("\n##################################################");
	}

	private void runSimulation() {
		System.out.println("Sample Scenario: Multiple fraudulent transactions "
				+ "by 2 cards in a given 24 hour sliding window");

		List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,90.10");
		//transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T15:10:50,90.50");
		//transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T10:15:54,33.00 ");
		//transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T18:45:23,15.75");
		//transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T11:12:54,33.00 ");
		//transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T18:43:10,30.00");

		System.out.println("\nSample Data:");
		transactionTextData.forEach(System.out::println);

		List<String> fraudulentCards = transactionAuditService
				.getCardsWithFraudulentBehaviour(transactionTextData, new BigDecimal(100));

		System.out.println("\nFraudulent crdit card hash: " + fraudulentCards);
	}
}
