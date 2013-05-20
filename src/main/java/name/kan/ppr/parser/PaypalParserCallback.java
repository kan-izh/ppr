package name.kan.ppr.parser;

import name.kan.ppr.model.txn.TxnStatus;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-01-25 19:25
 */
public interface PaypalParserCallback
{
	void createTxn(
			String txnRef,
			DateTime dateTime,
			String accountName,
			String type,
			TxnStatus status,
			Currency currency,
			BigDecimal gross,
			BigDecimal fee, final boolean credit);
}
