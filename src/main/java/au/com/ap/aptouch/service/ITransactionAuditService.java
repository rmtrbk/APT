package au.com.ap.aptouch.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for TransactionAuditService
 * 
 * @author Theja Kotuwella
 *
 */
public interface ITransactionAuditService {
	List<String> getCardsWithFraudulentBehaviour(List<String> transactionsTextData, 
															BigDecimal threshold);

}
