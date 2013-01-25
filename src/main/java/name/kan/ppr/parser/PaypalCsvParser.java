package name.kan.ppr.parser;


import com.google.common.collect.Maps;
import name.kan.ppr.model.tnx.TnxStatus;
import name.kan.ppr.model.tnx.TnxType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jumpmind.symmetric.csv.CsvReader;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author kan
 * @since 2013-01-25 19:16
 */
public class PaypalCsvParser
{
	private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");
	private CsvSettings settings;

	public void parse(final InputStream fis, final PaypalParserCallback callback) throws IOException
	{
		final CsvReader reader = new CsvReader(fis, Charset.forName("UTF-8"));
		reader.setDelimiter(getSettings().getDelimiter());
		if(!reader.readHeaders())
		{
			throw new EOFException("No headers");
		}
		final int txnRefCol = findCol(reader, "Transaction ID");
		final int dateCol = findCol(reader, "Date");
		final int timeCol = findCol(reader, "Time");
		final int tzCol = findCol(reader, "Time Zone");
		final int typeCol = findCol(reader, "Type");
		final int statusCol = findCol(reader, "Status");
		final int currencyCol = findCol(reader, "Currency");
		final int grossCol = findCol(reader, "Gross");
		final int feeCol = findCol(reader, "Fee");
		while(reader.readRecord())
		{
			final String tnxRef = getNotEmpty(reader, txnRefCol);
			final DateTime dateTime = parseDate(
					getNotEmpty(reader, dateCol),
					getNotEmpty(reader, timeCol),
					getNotEmpty(reader, tzCol));
			final TnxType type = parseType(getNotEmpty(reader, typeCol));
			final TnxStatus status = parseStatus(getNotEmpty(reader, statusCol));
			final Currency currency = parseCurrency(getNotEmpty(reader, currencyCol));
			final BigDecimal gross = new BigDecimal(getNotEmpty(reader, grossCol));
			final BigDecimal fee = new BigDecimal(getNotEmpty(reader, feeCol));
			callback.createTransaction(tnxRef, dateTime, type, status, currency, gross, fee);
		}
	}

	private Currency parseCurrency(final String notEmpty)
	{
		return Currency.getInstance(notEmpty);
	}

	private TnxStatus parseStatus(final String status) throws IOException
	{
		switch(status)
		{
		case "Completed": return TnxStatus.COMPLETED;
		default: throw new IOException("Unexpected status '"+status+"'");
		}
	}

	private TnxType parseType(final String type) throws IOException
	{
		switch(type)
		{
		case "Shopping Cart Payment Received": return TnxType.SHOPPING_CART_PAYMENT_RECEIVED;
		case "PayPal Express Checkout Payment Sent": return TnxType.PAYPAL_EXPRESS_CHECKOUT_PAYMENT_SENT;
		default: throw new IOException("Unexpected type '" + type + "'");
		}
	}

	private DateTime parseDate(final String date, final String time, final String tz)
	{
		final String text = date + " " + time;
		final DateTimeZone zone = TimezoneParser.findByShortName(tz);
		return dateTimeFormatter.withZone(zone).parseDateTime(text);
	}

	private String getNotEmpty(final CsvReader reader, final int txnRefCol) throws IOException
	{
		final String value = reader.get(txnRefCol).trim();
		if(value.isEmpty())
			throw new IOException("Row " + reader.getCurrentRecord() + " doesn't have Transaction ID");
		return value;
	}

	private int findCol(final CsvReader reader, final String headerName) throws IOException
	{
		final int txnRefCol = reader.getIndex(headerName);
		if(txnRefCol == -1)
			throw new IOException("Cannot find header '"+headerName+"'");
		return txnRefCol;
	}

	public CsvSettings getSettings()
	{
		return settings;
	}

	public void setSettings(final CsvSettings settings)
	{
		this.settings = settings;
	}

	private static class TimezoneParser
	{
		private static final Map<String, DateTimeZone> shortNames = Maps.newHashMap();
		static
		{
			for(final String id : TimeZone.getAvailableIDs())
			{
				final TimeZone tz = TimeZone.getTimeZone(id);
				final String nameW = tz.getDisplayName(false, TimeZone.SHORT);
				final String nameS = tz.getDisplayName(true, TimeZone.SHORT);
				final DateTimeZone dtz;
				if(shortNames.containsKey(nameS)
						|| shortNames.containsKey(nameW))
				{
					dtz = null;
				}
				else
				{
					try
					{
						dtz = DateTimeZone.forTimeZone(tz);
					} catch(IllegalArgumentException ignore)
					{
						continue;
					}
				}
				shortNames.put(nameW, dtz);
				shortNames.put(nameS, dtz);
			}
		}

		public static DateTimeZone findByShortName(final String name)
		{
			return shortNames.get(name);
		}
	}
}
