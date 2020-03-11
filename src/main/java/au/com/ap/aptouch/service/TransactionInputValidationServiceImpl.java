package au.com.ap.aptouch.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.ap.aptouch.exception.TransactionFormatException;
import au.com.ap.aptouch.util.ApplicationPropertyManager;

/**
 * Validates transaction text input
 * 
 * @author Theja Kotuwella
 *
 */
@Service
public class TransactionInputValidationServiceImpl 
								implements ITransactionInputValidationService {
	@Autowired
	ApplicationPropertyManager properties;
	
	/**
	 * Validates 'wellformedness' of transaction text input
	 * 
	 * @param transactionText Textual representation of a transaction
	 * @throws TransactionFormatException
	 */
	public void validateTransactionInput(String transactionText) 
											throws TransactionFormatException {
		checkNullAndEmpty(transactionText);
		checkCorrectLength(transactionText);
		checkCorrectTimestampFormat(transactionText);
		checkForThresholdTypeBigDecimal(transactionText);
	}
	
	private void checkNullAndEmpty(String transactionText) 
											throws TransactionFormatException {
		if(transactionText == null) {
			throw new TransactionFormatException("Transaction text is NULL");
			
		} else if(transactionText.isEmpty()) {
			throw new TransactionFormatException("Transaction text is empty");
		}
	}
	
	private void checkCorrectLength(String transactionText) 
											throws TransactionFormatException {
		String[] transactionValues =  transactionText.trim().split(properties.SPLIT_REG_X);
		
		if(transactionValues.length != 3) {
			throw new TransactionFormatException("Incorrect number of arguments"
					+ " passed in transaction text");
		}
	}
	
	private void checkCorrectTimestampFormat(String transactionText) 
											throws TransactionFormatException {
		String[] transactionValues =  transactionText.trim().split(properties.SPLIT_REG_X);

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(properties.DATE_FORMAT);
			LocalDateTime.parse(transactionValues[properties.TIME_STAMP_POSITION], 
									formatter);
			
        } catch (DateTimeParseException e) {
        	throw new TransactionFormatException("Incorrect date time format"
					+ " passed in transaction text");
        }
	}
	
	private void checkForThresholdTypeBigDecimal(String transactionText) 
											throws TransactionFormatException {
		String[] transactionValues =  transactionText.trim().split(properties.SPLIT_REG_X);
		
		try {
			new BigDecimal(transactionValues[properties.THRESHOLD_POSITION]);
			
		} catch(NumberFormatException e) {
			throw new TransactionFormatException("Incorrect threshold value"
					+ " passed in transaction text");
		}
	}
}
