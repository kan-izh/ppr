package name.kan.ppr.parser;

import name.kan.ppr.model.account.AccountEntity;
import name.kan.ppr.model.account.AccountRepository;
import name.kan.ppr.model.txn.TxnEntity;
import name.kan.ppr.model.txn.TxnRepository;
import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnTypeEntity;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-02-16 00:41
 */
public class JdbcPaypalParserCallbackTest
{
	private static final String A_TYPE_NAME = "a type name";
	private static final String AN_ACCOUNT_NAME = "an account name";
	@InjectMocks
	private final JdbcPaypalParserCallback callback = new JdbcPaypalParserCallback();

	@Mock TxnTypeRepository txnTypeRepository;

	@Mock TxnRepository txnRepository;

	@Mock TxnTypeEntity txnType;

	@Mock AccountEntity accountEntity;

	@Mock AccountRepository accountRepository;

	@Captor ArgumentCaptor<TxnEntity> entityCaptor;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		when(txnTypeRepository.obtainByName(A_TYPE_NAME)).thenReturn(txnType);
		when(accountRepository.obtainByName(AN_ACCOUNT_NAME)).thenReturn(accountEntity);
	}

	@Test
	public void testCreateTxn() throws Exception
	{
		final DateTime dateTime = DateTime.now();
		final Currency currency = Currency.getInstance("GBP");
		final BigDecimal gross = BigDecimal.valueOf(12.34);
		final BigDecimal fee = BigDecimal.valueOf(23.45);
		callback.createTxn(
				"ref",
				dateTime,
				AN_ACCOUNT_NAME,
				A_TYPE_NAME,
				TxnStatus.COMPLETED,
				currency,
				gross,
				fee,
				true);
		verify(txnRepository)
				.save(entityCaptor.capture());
		final TxnEntity entity = entityCaptor.getValue();
		assertEquals("ref", entity.getReference());
		assertEquals(dateTime, entity.getDateTime());
		assertEquals(txnType, entity.getType());
		assertEquals(accountEntity, entity.getAccount());
		assertEquals(TxnStatus.COMPLETED, entity.getStatus());
		assertEquals(currency, entity.getCurrency());
		assertEquals(gross, entity.getGross());
		assertEquals(fee, entity.getFee());
		assertEquals(true, entity.isCredit());
	}
}
