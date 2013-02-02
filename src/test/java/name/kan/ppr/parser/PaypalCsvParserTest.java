package name.kan.ppr.parser;

import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnType;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-01-25 19:16
 */
public class PaypalCsvParserTest
{
	@InjectMocks
	final PaypalCsvParser parser = new PaypalCsvParser();

	@Mock
	PaypalParserCallback callback;

	@Mock
	TxnType txnType;

	@Mock
	TxnTypeRepository txnTypeRepository;

	@Spy
	CsvSettings csvSettings = new CsvSettings();

	public PaypalCsvParserTest()
	{
		MockitoAnnotations.initMocks(this);
		csvSettings.setDelimiter('\t');
	}

	@Before
	public void setUp() throws Exception
	{
		when(txnTypeRepository.obtainByName("Shopping Cart Payment Received")).thenReturn(txnType);
	}

	@Test
	public void parseCsv() throws Exception
	{
		try(final InputStream fis = getClass().getResourceAsStream("test.csv"))
		{
			parser.parse(fis, callback);
		}
		verify(callback).createTxn(
				"54052958PJ614430V",
				new DateTime("2011-04-01T18:23:36", DateTimeZone.forID("Europe/London")),
				txnType,
				TxnStatus.COMPLETED,
				Currency.getInstance("GBP"),
				BigDecimal.valueOf(23),
				BigDecimal.valueOf(-0.98)
		);
	}
}
