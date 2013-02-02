package name.kan.ppr.parser;


import com.google.common.collect.Maps;
import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnType;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jumpmind.symmetric.csv.CsvReader;

import javax.inject.Inject;
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
	private TxnTypeRepository txnTypeRepository;

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
			final String txnRef = getNotEmpty(reader, txnRefCol);
			final DateTime dateTime = parseDate(
					getNotEmpty(reader, dateCol),
					getNotEmpty(reader, timeCol),
					getNotEmpty(reader, tzCol));
			final TxnType type = parseType(getNotEmpty(reader, typeCol));
			final TxnStatus status = parseStatus(getNotEmpty(reader, statusCol));
			final Currency currency = parseCurrency(getNotEmpty(reader, currencyCol));
			final BigDecimal gross = new BigDecimal(getNotEmpty(reader, grossCol));
			final BigDecimal fee = new BigDecimal(getNotEmpty(reader, feeCol));
			callback.createTxn(txnRef, dateTime, type, status, currency, gross, fee);
		}
	}

	private Currency parseCurrency(final String notEmpty)
	{
		return Currency.getInstance(notEmpty);
	}

	private TxnStatus parseStatus(final String status) throws IOException
	{
		switch(status)
		{
		case "Completed": return TxnStatus.COMPLETED;
		case "Cancelled": return TxnStatus.CANCELLED;
		case "Refunded": return TxnStatus.REFUNDED;
		case "Removed": return TxnStatus.REMOVED;
		default: throw new IOException("Unexpected status '"+status+"'");
		}
	}

	private TxnType parseType(final String type) throws IOException
	{
		return getTxnTypeRepository().obtainByName(type);
	}

	private DateTime parseDate(final String date, final String time, final String tz)
	{
		final String text = date + " " + time;
		final DateTimeZone zone = TimezoneParser.findByShortName(tz);
		return dateTimeFormatter.withZone(zone).parseDateTime(text);
	}

	private String getNotEmpty(final CsvReader reader, final int headerIdx) throws IOException
	{
		final String rawValue = reader.get(headerIdx);
		final String value = rawValue == null ? null : rawValue.trim();
		if(value == null || value.isEmpty())
			throw new IOException("Row " + (reader.getCurrentRecord() + 1)
					+ " doesn't have '"+reader.getHeader(headerIdx)+"'");
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

	@Inject
	public void setSettings(final CsvSettings settings)
	{
		this.settings = settings;
	}

	public TxnTypeRepository getTxnTypeRepository()
	{
		return txnTypeRepository;
	}

	@Inject
	public void setTxnTypeRepository(final TxnTypeRepository txnTypeRepository)
	{
		this.txnTypeRepository = txnTypeRepository;
	}

	private static class TimezoneParser
	{
		private final Map<String, DateTimeZone> shortNames = Maps.newHashMap();
		private static final TimezoneParser instance = new TimezoneParser();

		private TimezoneParser()
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
			return instance.shortNames.get(name);
		}
	}
}
