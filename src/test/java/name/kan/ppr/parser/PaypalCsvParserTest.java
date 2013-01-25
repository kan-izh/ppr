package name.kan.ppr.parser;

import name.kan.ppr.model.tnx.TnxStatus;
import name.kan.ppr.model.tnx.TnxType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.verify;

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

	CsvSettings csvSettings = new CsvSettings();

	public PaypalCsvParserTest()
	{
		MockitoAnnotations.initMocks(this);
		csvSettings.setDelimiter('\t');
		parser.setSettings(csvSettings);
	}

	@Test
	public void parseCsv() throws Exception
	{
		try(final InputStream fis = getClass().getResourceAsStream("test.csv"))
		{
			parser.parse(fis, callback);
		}
		verify(callback).createTransaction(
				"54052958PJ614430V",
				new DateTime("2011-04-01T18:23:36", DateTimeZone.forID("Europe/London")),
				TnxType.SHOPPING_CART_PAYMENT_RECEIVED,
				TnxStatus.COMPLETED,
				Currency.getInstance("GBP"),
				BigDecimal.valueOf(23),
				BigDecimal.valueOf(-0.98)
		);
	}
}
