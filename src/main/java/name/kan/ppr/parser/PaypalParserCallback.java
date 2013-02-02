package name.kan.ppr.parser;

import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnType;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-01-25 19:25
 */
public interface PaypalParserCallback
{
	long createTxn(String txnRef, DateTime dateTime, TxnType type, TxnStatus status, Currency currency, BigDecimal gross, BigDecimal fee);
}
