package name.kan.ppr.parser;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.account.AccountRepository;
import name.kan.ppr.model.txn.TxnEntity;
import name.kan.ppr.model.txn.TxnRepository;
import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-02-01 21:18
 */
public class JdbcPaypalParserCallback implements PaypalParserCallback
{
	@Inject
	private TxnTypeRepository txnTypeRepository;
	@Inject
	private TxnRepository txnRepository;
	@Inject
	private AccountRepository accountRepository;

	@Transactional(readOnly = false)
	@Override
	public void createTxn(final String txnRef, final DateTime dateTime, final String accountName, final String type, final TxnStatus status, final Currency currency, final BigDecimal gross, final BigDecimal fee, final boolean credit)
	{
		final TxnEntity entity = new TxnEntity();
		entity.setReference(txnRef);
		entity.setAccount(accountRepository.obtainByName(accountName));
		entity.setDateTime(dateTime);
		entity.setType(txnTypeRepository.obtainByName(type));
		entity.setStatus(status);
		entity.setCurrency(currency);
		entity.setGross(gross);
		entity.setFee(fee);
		entity.setCredit(credit);
		txnRepository.save(entity);
	}
}
