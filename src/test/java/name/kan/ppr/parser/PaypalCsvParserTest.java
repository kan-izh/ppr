package name.kan.ppr.parser;

import name.kan.ppr.model.txn.TxnStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Currency;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author kan
 * @since 2013-01-25 19:16
 */
public class PaypalCsvParserTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	final PaypalCsvParser parser = new PaypalCsvParser();

	@Mock
	PaypalParserCallback callback;

	@Spy
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
		verify(callback).createTxn(
				"54052958PJ614430V",
				new DateTime("2011-04-01T18:23:36", DateTimeZone.forID("Europe/London")),
				"melanie hammond",
				"Shopping Cart Payment Received",
				TxnStatus.COMPLETED,
				Currency.getInstance("GBP"),
				BigDecimal.valueOf(23),
				BigDecimal.valueOf(-0.98),
				true
		);
	}

	@Test(expected = EOFException.class)
	public void testEmptyInput() throws Exception
	{
		parser.parse(new ByteArrayInputStream("".getBytes()), callback);
	}

	@Test
	public void testMissingHeader() throws Exception
	{
		expectedException.expectMessage(containsString("'Date'"));
		try(final InputStream fis = getClass().getResourceAsStream("missing-header.csv"))
		{
			parser.parse(fis, callback);
		}
	}

	@Test
	public void testMissingValue() throws Exception
	{
		expectedException.expectMessage(containsString("'Status'"));
		expectedException.expectMessage(containsString("Row 1"));
		try(final InputStream fis = getClass().getResourceAsStream("missing-value.csv"))
		{
			parser.parse(fis, callback);
		}
	}

	@Test
	public void testAllStatuses() throws Exception
	{
		try(final InputStream fis = getClass().getResourceAsStream("all-statuses.csv"))
		{
			parser.parse(fis, callback);
		}
		for(final TxnStatus status : TxnStatus.values())
			verify(callback).createTxn(
					anyString(),
					Matchers.<DateTime>any(),
					anyString(),
					anyString(),
					eq(status),
					Matchers.<Currency>any(),
					Matchers.<BigDecimal>any(),
					Matchers.<BigDecimal>any(),
					anyBoolean());
	}

	@Test
	public void testBadStatus() throws Exception
	{
		expectedException.expectMessage(containsString("BadStatus"));
		try(final InputStream fis = getClass().getResourceAsStream("bad-status.csv"))
		{
			parser.parse(fis, callback);
		}
	}
}
