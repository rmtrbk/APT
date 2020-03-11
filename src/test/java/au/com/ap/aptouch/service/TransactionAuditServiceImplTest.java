package au.com.ap.aptouch.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.ap.aptouch.service.TransactionAuditServiceImpl;

/**
 * This class contains all test cases for testing 
 * {@link au.com.ap.aptouch.service.TransactionAuditServiceImpl}
 * 
 * @author Theja Kotuwella
 *
 */
@SpringBootTest
public class TransactionAuditServiceImplTest {
	@Autowired
	TransactionAuditServiceImpl transactionAuditService;

	@ParameterizedTest(name="Run {index}: transactionTextData={0}, threshold={1}")
    @MethodSource({"singleCardSingleDaySingleTransactionPositive_Parameters",
    				"singleCardSingleDaySingleTransactionSpaceInValuesPositive_Parameters",
    				"singleCardSingleDayMultipleTransactionsPositive_Parameters",
    				"singleCardSingleDayMultipleTransactionsSpaceInValuesPositive_Parameters",
    				"singleCardValidSlidingWindowMultipleTransactionsPositive_Parameters",
    				"singleCardSingleDayMultipleTransactionsOneEmptyValuePositive_Parameters",
    				"singleCardSingleDayMultipleTransactionsPositive2Days_Parameters"
    				})
    public void testGetCardsWithFraudulentBehaviour_SingleCardPositive
    							(List<String> transactionTextData, 
    							BigDecimal threshold) throws Throwable {
		List<String> fraudulentCards = transactionAuditService
				.getCardsWithFraudulentBehaviour(transactionTextData, threshold);
		
		assertTrue(fraudulentCards.isEmpty());
	}
	
	@ParameterizedTest(name="Run {index}: transactionTextData={0}, threshold={1}")
    @MethodSource({"singleCardSingleDaySingleTransactionNegative_Parameters",
    				"singleCardSingleDaySingleTransactionSpaceInValuesNegative_Parameters",
    				"singleCardSingleDayMultipleTransactionsNegative_Parameters",
    				"singleCardSingleDayMultipleTransactionsSpaceInValuesNegative_Parameters",
    				"singleCardValidSlidingWindowMultipleTransactionsNegative_Parameters"
    				})
    public void testGetCardsWithFraudulentBehaviour_SingleCardNegative
    										(List<String> transactionTextData, 
    										BigDecimal threshold) throws Throwable {
		List<String> fraudulentCards = transactionAuditService.getCardsWithFraudulentBehaviour(transactionTextData, threshold);
		
		assertTrue("10d7ce2f43e35fa57d1bbf8b1e1".contentEquals(fraudulentCards.get(0)));
	}
	
	@ParameterizedTest(name="Run {index}: transactionTextData={0}, threshold={1}")
    @MethodSource({"multipleCardSingleDaySingleTransactionPositive_Parameters",
    				"multipleCardSingleDaySingleTransactionSpaceInValuesPositive_Parameters",
    				"multipleCardSingleDayMultipleTransactionsPositive_Parameters",
    				"multipleCardSingleDayMultipleTransactionsSpaceInValuesPositive_Parameters",
    				"multipleCardValidSlidingWindowMultipleTransactionsPositive_Parameters",
    				"multipleCardSingleDayMultipleTransactionsNullValuesPositive_Parameters"
    				})
    public void testGetCardsWithFraudulentBehaviour_MultipleCardPositive
    										(List<String> transactionTextData, 
    										BigDecimal threshold) throws Throwable {
		List<String> fraudulentCards = transactionAuditService
				.getCardsWithFraudulentBehaviour(transactionTextData, threshold);
		
		assertTrue(fraudulentCards.isEmpty());
	}
	
	@ParameterizedTest(name="Run {index}: transactionTextData={0}, threshold={1}")
    @MethodSource({"multipleCardSingleDaySingleTransactionNegative_Parameters",
    				"multipleCardSingleDaySingleTransactionSpaceInValuesNegative_Parameters",
    				"multipleCardSingleDayMultipleTransactionsNegative_Parameters",
    				"multipleCardSingleDayMultipleTransactionsSpaceInValuesNegative_Parameters",
    				"multipleCardValidSlidingWindowMultipleTransactionsNegative_Parameters"
    				})
    public void testGetCardsWithFraudulentBehaviour_MultipleCardNegative
    										(List<String> transactionTextData, 
    										BigDecimal threshold) throws Throwable {
		List<String> fraudulentCards = transactionAuditService
				.getCardsWithFraudulentBehaviour(transactionTextData, threshold);
		
		assertTrue(fraudulentCards.contains("10d7ce2f43e35fa57d1bbf8b1e1"));
		assertTrue(fraudulentCards.contains("5910f4ea0062a0e29afd3dccc741e3ce"));
	}
	
	//------------- Basic Scenarios - Start of DATA FOR SINGLE CARD ------------

    static Stream<Arguments> singleCardSingleDaySingleTransactionPositive_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T13:15:54,10.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDaySingleTransactionNegative_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T13:15:54,101.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDaySingleTransactionSpaceInValuesPositive_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add(" 10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-29T13:15:54,10.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDaySingleTransactionSpaceInValuesNegative_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add(" 10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-29T13:15:54,102.00 ");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    			);
    }
    
    static Stream<Arguments> singleCardSingleDayMultipleTransactionsPositive_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,20.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T04:15:54,33.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDayMultipleTransactionsNegative_Parameters() 
    														throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,20.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T04:15:54,133.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDayMultipleTransactionsPositive2Days_Parameters() 
    		throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T12:11:52,90.10");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    			);
    }
    
    static Stream<Arguments> singleCardSingleDayMultipleTransactionsSpaceInValuesPositive_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add(" 10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1 ,2014-04-29T12:11:52,20.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1 ,2014-04-30T04:15:54 ,33.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDayMultipleTransactionsSpaceInValuesNegative_Parameters() 
    														throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add(" 10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1 ,2014-04-29T12:11:52,120.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1 ,2014-04-30T04:15:54 ,133.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(100.00))
        );
    }
    
    static Stream<Arguments> singleCardSingleDayMultipleTransactionsOneEmptyValuePositive_Parameters() 
    		throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add("");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T04:15:54,33.00 ");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    	);
    }
    
    //-------------------------- End of DATA FOR SINGLE CARD -------------------
	
	//--------- Basic Scenarios - Start of DATA FOR MULTIPLE CARDs -------------

    static Stream<Arguments> multipleCardSingleDaySingleTransactionPositive_Parameters() 
    														throws Throwable {
    	// Individually does not exceed, but collectively exceed.
    	// One is equal to threshold
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T13:15:54,10.10");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2020-12-05T01:25:33,1200.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDaySingleTransactionNegative_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T13:15:54,2000.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2020-12-05T01:25:33,1200.10");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDaySingleTransactionSpaceInValuesPositive_Parameters() 
    														throws Throwable {
    	// Individually does not exceed, but collectively exceed.
    	// One is equal to threshold
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-29T13:15:54,10.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2020-12-05T01:25:33, 1200.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDaySingleTransactionSpaceInValuesNegative_Parameters() 
    														throws Throwable {
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1 , 2014-04-29T13:15:54,2000.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce, 2020-12-05T01:25:33, 3200.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDayMultipleTransactionsPositive_Parameters() 
    														throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	// Card transactions appear in a mixed order
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T06:11:52,120.50");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T04:15:54,33.75");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T12:11:52,100.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T15:15:54,33.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDayMultipleTransactionsNegative_Parameters() 
    														throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	// Card transactions appear in a mixed order
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,1190.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T06:11:52,120.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T04:15:54,10.23");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T12:11:52,100.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T15:15:54,1333.00");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDayMultipleTransactionsSpaceInValuesPositive_Parameters() 
    														throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	// Card transactions appear in a mixed order
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add(" 10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce , 2014-04-29T06:11:52, 120.50 ");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-30T04:15:54,33.75");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce ,2014-04-30T12:11:52,100.00");
		transactionTextData.add(" 5910f4ea0062a0e29afd3dccc741e3ce, 2014-04-30T15:15:54,33.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDayMultipleTransactionsSpaceInValuesNegative_Parameters() 
    														throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	// Card transactions appear in a mixed order
    	List<String> transactionTextData = new ArrayList<>();
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-29T05:10:50, 1190.00");
		transactionTextData.add(" 5910f4ea0062a0e29afd3dccc741e3ce, 2014-04-29T06:11:52,120.00");
		transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-30T04:15:54,10.23");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T12:11:52,100.00");
		transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T15:15:54, 1333.00 ");
		
        return Stream.of(
            Arguments.of(transactionTextData, new BigDecimal(1200.00))
        );
    }
    
    static Stream<Arguments> multipleCardSingleDayMultipleTransactionsNullValuesPositive_Parameters() 
    		throws Throwable {
    	// 24 hour slide occurs in 2 days, not in the same day
    	// Card transactions appear in a mixed order
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add(" 10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add(null);
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T04:15:54,33.75");
    	transactionTextData.add(null);
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T15:15:54,33.00 ");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(1200.00))
    	);
    }
    
    //------------------------ End of DATA FOR MULTIPLE CARDS ------------------
    
    //------------------------ Complex Scenarios -------------------------------
    static Stream<Arguments> singleCardValidSlidingWindowMultipleTransactionsPositive_Parameters() 
    		throws Throwable {
    	// Below data has 3 sliding windows of 24 hours, all positive
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,20.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T10:15:54,33.00 ");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T11:12:54,33.00 ");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    	);
    }
    
    static Stream<Arguments> singleCardValidSlidingWindowMultipleTransactionsNegative_Parameters() 
    		throws Throwable {
    	// Below data has 3 sliding windows of 24 hours, 2 positive, 1 negative
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,20.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T10:15:54,33.00 ");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T11:12:54,133.00 ");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    	);
    }
    
    static Stream<Arguments> multipleCardValidSlidingWindowMultipleTransactionsPositive_Parameters() 
    		throws Throwable {
    	// Below data has 3 sliding windows of 24 hours, all positive
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,20.00");
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T15:10:50,5.50");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T10:15:54,33.00 ");
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T18:45:23,15.75");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T11:12:54,33.00 ");
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T18:43:10,30.00");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    	);
    }
    
    static Stream<Arguments> multipleCardValidSlidingWindowMultipleTransactionsNegative_Parameters() 
    		throws Throwable {
    	// Below data has 3 sliding windows of 24 hours, 2 positive, 1 negative
    	List<String> transactionTextData = new ArrayList<>();
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T05:10:50,10.00");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T12:11:52,90.10");
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T15:10:50,90.50");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T10:15:54,33.00 ");
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-29T18:45:23,15.75");
    	transactionTextData.add("10d7ce2f43e35fa57d1bbf8b1e1,2014-04-30T11:12:54,33.00 ");
    	transactionTextData.add("5910f4ea0062a0e29afd3dccc741e3ce,2014-04-30T18:43:10,30.00");

    	return Stream.of(
    			Arguments.of(transactionTextData, new BigDecimal(100.00))
    	);
    }
}
