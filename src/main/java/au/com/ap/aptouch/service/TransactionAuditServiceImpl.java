package au.com.ap.aptouch.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.ap.aptouch.dto.TransactionDTO;
import au.com.ap.aptouch.exception.TransactionFormatException;
import au.com.ap.aptouch.util.ApplicationPropertyManager;

/**
 * Services auditing of credit card transactions
 * 
 * @author Theja Kotuwella
 *
 */
@Service
public class TransactionAuditServiceImpl implements ITransactionAuditService {
	@Autowired
	ApplicationPropertyManager properties;
	
	@Autowired
	TransactionInputValidationServiceImpl validationService;
	
	/**
	 * Finds the hash value(s) of the credit card(s) that have involved in 
	 * fraudulent behaviour.
	 * 
	 * @param transactionsTextData List of transactions in text format
	 * @param threshold Daily threshold value for the list of transactions provided
	 * @return Hash value of the credit card(s) that have exceeded threshold
	 */
	@Override
	public List<String> getCardsWithFraudulentBehaviour(List<String> transactionsTextData, 
												BigDecimal threshold) {
		// Map textual data to a DTO
		List<TransactionDTO> transactionObjects = 
				convertTextInputToTransactionObjects(transactionsTextData);
		
		// Group transaction objects by credit card hash
		Map<String, List<TransactionDTO>> transactionsByCardHash = 
				groupTransactionByCreditCardHash(transactionObjects);
		
		// Find credit card hash where daily transactions have exceeded given threshold
		Set<String> uniqueFraudulentCardHashes = 
			getUniqueCardHashWithFraudulentBehaviour(transactionsByCardHash, threshold);
		
		return new ArrayList<>(uniqueFraudulentCardHashes);
	}
	
	/**
	 * Accepts a list of textual data representing transactions.
	 * For example text representing a single transaction would look like below
	 * "10d7ce2f43e35fa57d1bbf8b1e1, 2014-04-29T13:15:54, 10.00"
	 * That is "<credit_card_hash>, <time_stamp>, <threshold>"
	 * 
	 * @param transactions List of transactions in text format
	 * @return List of Transaction Data Transfer Object(DTO)
	 */
	private List<TransactionDTO> 
				convertTextInputToTransactionObjects(List<String> transactions) {
		List<TransactionDTO> transactionObjects = new ArrayList<>();
		
		transactions.forEach(transaction -> {
			try {
				validationService.validateTransactionInput(transaction);
				
				String[] transactionValues 	= transaction.trim().split(properties.SPLIT_REG_X);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(properties.DATE_FORMAT);
				LocalDateTime timestamp 	= LocalDateTime.parse(
									transactionValues[properties.TIME_STAMP_POSITION], 
									formatter);
				
				TransactionDTO dto = new TransactionDTO()
					.setCardHash(transactionValues[properties.CREDIT_CARD_HASH_POSITION])
					.setTimestamp(timestamp)
					.setAmount(new BigDecimal(transactionValues[properties.THRESHOLD_POSITION]));
				
				transactionObjects.add(dto);
				
			} catch (TransactionFormatException e) {
				e.printStackTrace();
			}
		});
		return transactionObjects;
	}
	
	/**
	 * Groups all transactions belonging to a given credit card hash
	 * 
	 * @param transactions List of Transaction Data Transfer Objects(DTOs)
	 * @return Map organised with list of transactions against relevant 
	 * credit card hash 
	 */
	private Map<String, List<TransactionDTO>> 
			groupTransactionByCreditCardHash(List<TransactionDTO> transactions) {
		Map<String, List<TransactionDTO>> transactionsByCardHash = transactions.stream() 
					.collect(Collectors.groupingBy(TransactionDTO::getCardHash));
					
		return transactionsByCardHash;
	}
	
	/**
	 * Accepts transactions grouped by their relevant credit card hash 
	 * and iterates each and every credit card hash. While iterating it takes
	 * all transactions for the selected credit card hash, 
	 * filters transactions for next 24 hours and adds up spending for the 24 hours.
	 * If the spending for the day exceeds the given threshold, 
	 * the credit card hash is added to the fraudulent set of credit cards.
	 * If a single card has been involved in fraud in multiple 24 hour slots, 
	 * the credit card hash will only appear once.
	 * 
	 * @param transactionsByCardHash Transactions grouped against credit card hash
	 * @param threshold Daily limit for spending for the given set of transactions
	 * @return Unique set of credit card hash values
	 */
	private Set<String> getUniqueCardHashWithFraudulentBehaviour
	(Map<String, List<TransactionDTO>> transactionsByCardHash,
			BigDecimal threshold) {
		Set<String> uniqueCardHashes = new HashSet<>();
		
		// Get transactions for each card hash
		// Iterate each and every transaction (transactionInConsideration)
		// For each transactionInConsideration get all other transactions within
		// next 24 hours(all of transactionInFuture)
		// Add up all transactionInFuture spending amounts and check with threshold
		transactionsByCardHash.forEach((cardHash, transactionsByCard) -> {
			transactionsByCard.forEach(transactionInConsideration -> {
				// Get future transactions
				List<TransactionDTO> restOfTransactionsByCard = transactionsByCard.stream()
						.skip(transactionsByCard.indexOf(transactionInConsideration) + 1)
						.collect(Collectors.toList());
				
				// Filter future transactions in next 24 hours
				List<TransactionDTO> transactionIn24HoursForACard = restOfTransactionsByCard.stream()               
						.filter(transactionInFuture -> 
							(transactionInFuture.getTimestamp().minus(24 , 
									ChronoUnit.HOURS))
									.isBefore(transactionInConsideration.getTimestamp()))    
						.collect(Collectors.toList()); 
				
				// Add amounts that appear within next 24 hours
				BigDecimal spendingIn24Hours = transactionIn24HoursForACard.stream()
						.map(TransactionDTO::getAmount)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
				
				// Add the considered transactions values to the next 24 hour values
				spendingIn24Hours = spendingIn24Hours.add(transactionInConsideration.getAmount());

				// Fraudulent: spending within 24 hours has exceeded threshold
				if(threshold.compareTo(spendingIn24Hours) < 0) {
					uniqueCardHashes.add(cardHash);
				}
			});
		});
		return uniqueCardHashes;
	}
}
