package au.com.ap.aptouch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.ap.aptouch.exception.TransactionFormatException;
import au.com.ap.aptouch.service.TransactionInputValidationServiceImpl;

/**
 * This class contains all test cases for testing 
 * {@link au.com.ap.aptouch.service.TransactionInputValidationServiceImpl}
 * 
 * @author Theja Kotuwella
 *
 */
@SpringBootTest
public class TransactionInputValidationServiceImplTest {
	@Autowired
	TransactionInputValidationServiceImpl validationService;

	@Test
	public void testValidateTransactionInput_Null() {
	    Throwable exception = assertThrows(TransactionFormatException.class, 
	    				() -> validationService.validateTransactionInput(null));
	    
	    assertEquals("Transaction text is NULL", exception.getMessage());
	}
	
	@Test
	public void testValidateTransactionInput_Empty() {
	    Throwable exception = assertThrows(TransactionFormatException.class, 
	    				() -> validationService.validateTransactionInput(""));
	    
	    assertEquals("Transaction text is empty", exception.getMessage());
	}
	
	@Test
	public void testValidateTransactionInput_TooLessArguments() {
	    Throwable exception = assertThrows(TransactionFormatException.class, 
	    		() -> validationService.validateTransactionInput(
	    								"10d7ce2f43e35fa57d1bbf8b1e1,101.00"));
	    
	    assertEquals("Incorrect number of arguments passed in transaction text", 
	    												exception.getMessage());
	}
	
	@Test
	public void testValidateTransactionInput_TooManyArguments() {
	    Throwable exception = assertThrows(TransactionFormatException.class, 
	    		() -> validationService.validateTransactionInput(
	    								"10d7ce2f43e35fa57d1bbf8b1e1,101.00,1,abc"));
	    
	    assertEquals("Incorrect number of arguments passed in transaction text", 
	    												exception.getMessage());
	}
	
	@Test
	public void testValidateTransactionInput_IncorrectTime() {
	    Throwable exception = assertThrows(TransactionFormatException.class, 
	    		() -> validationService.validateTransactionInput(
	    				"10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T25:15:54,101.00"));
	    
	    assertEquals("Incorrect date time format passed in transaction text", 
	    												exception.getMessage());
	}
	
	@Test
	public void testValidateTransactionInput_IncorrectThreshold() {
	    Throwable exception = assertThrows(TransactionFormatException.class, 
	    		() -> validationService.validateTransactionInput(
	    				"10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T21:15:54,abc"));
	    
	    assertEquals("Incorrect threshold value passed in transaction text", 
	    												exception.getMessage());
	}
	
	@Test
	public void testValidateTransactionInput_CorrectArgumentsWithoutSpace() {
		assertDoesNotThrow(() -> 
			validationService.validateTransactionInput(
					"10d7ce2f43e35fa57d1bbf8b1e1,2014-04-29T13:15:54,101.00"));
	}
	
	@Test
	public void testValidateTransactionInput_CorrectArgumentsWithSpace() {
		assertDoesNotThrow(() -> 
			validationService.validateTransactionInput(
					" 10d7ce2f43e35fa57d1bbf8b1e1 , 2014-04-29T13:15:54 , 101.00 "));
	}
}
