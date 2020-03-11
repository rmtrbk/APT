package au.com.ap.aptouch.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Maintains and provides all application properties
 * 
 * @author Theja Kotuwella
 *
 */
@Component
public class ApplicationPropertyManager {
	public String SPLIT_REG_X = "\\s*,\\s*";
	
	@Value("${transactionauditservice.creditcardhash.position}")
	public int CREDIT_CARD_HASH_POSITION;
	
	@Value("${transactionauditservice.timestamp.position}")
	public int TIME_STAMP_POSITION;
	
	@Value("${transactionauditservice.threshold.position}")
	public int THRESHOLD_POSITION;
	
	@Value("${transactionauditservice.timestamp.format}")
	public String DATE_FORMAT;
}
