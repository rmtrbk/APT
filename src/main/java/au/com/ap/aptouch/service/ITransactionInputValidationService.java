package au.com.ap.aptouch.service;

import au.com.ap.aptouch.exception.TransactionFormatException;

/**
 * Interface of transaction textual input validation service
 * 
 * @author Theja Kotuwella
 *
 */
public interface ITransactionInputValidationService {
	void validateTransactionInput(String transactionText) 
											throws TransactionFormatException;
}
