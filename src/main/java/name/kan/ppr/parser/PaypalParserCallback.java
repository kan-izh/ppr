package name.kan.ppr.parser;

import name.kan.ppr.model.tnx.TnxStatus;
import name.kan.ppr.model.tnx.TnxType;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-01-25 19:25
 */
public interface PaypalParserCallback
{
	void createTransaction(String txnRef, DateTime dateTime, TnxType type, TnxStatus status, Currency currency, BigDecimal gross, BigDecimal fee);
}
