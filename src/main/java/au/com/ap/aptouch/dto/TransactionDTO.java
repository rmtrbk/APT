package au.com.ap.aptouch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Data Transfer Object
 * 
 * @author Theja Kotuwella
 *
 */
public class TransactionDTO {
	private String cardHash;
	private BigDecimal amount;
	private LocalDateTime timestamp;
	
	public String getCardHash() {
		return cardHash;
	}
	public TransactionDTO setCardHash(String cardHash) {
		this.cardHash = cardHash;
		return this;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public TransactionDTO setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public TransactionDTO setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}
}
