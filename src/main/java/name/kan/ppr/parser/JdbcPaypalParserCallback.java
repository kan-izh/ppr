package name.kan.ppr.parser;

import com.google.inject.name.Named;
import name.kan.jdbc.SequenceGenerator;
import name.kan.jdbc.Transactional;
import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnType;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Provider;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Currency;
import java.util.TimeZone;

/**
 * @author kan
 * @since 2013-02-01 21:18
 */
public class JdbcPaypalParserCallback implements PaypalParserCallback
{
	private static final String CREATE_TXN_SQL =
			"INSERT INTO txn(\n" +
					"            id, ref, date_time, txn_type_id, status, currency, gross, fee)\n" +
					"    VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

	private Provider<Connection> connectionProvider;

	private SequenceGenerator txnSequenceGenerator;

	@Transactional
	@Override
	public long createTxn(final String txnRef, final DateTime dateTime, final TxnType type, final TxnStatus status, final Currency currency, final BigDecimal gross, final BigDecimal fee)
	{
		final Connection connection = getConnectionProvider().get();
		try(final PreparedStatement statement = connection.prepareStatement(CREATE_TXN_SQL))
		{
			final long id = getTxnSequenceGenerator().next();
			statement.setLong(1, id);
			statement.setString(2, txnRef);
			statement.setDate(3, new Date(dateTime.getMillis()), UTC_CALENDAR);
			statement.setLong(4, type.getId());
			statement.setInt(5, status.ordinal());
			statement.setInt(6, currency.getNumericCode());
			statement.setBigDecimal(7, gross);
			statement.setBigDecimal(8, fee);
			statement.execute();
			return id;
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Provider<Connection> getConnectionProvider()
	{
		return connectionProvider;
	}

	@Inject
	public void setConnectionProvider(final Provider<Connection> connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public SequenceGenerator getTxnSequenceGenerator()
	{
		return txnSequenceGenerator;
	}

	@Inject@Named("tnx")
	public void setTxnSequenceGenerator(final SequenceGenerator txnSequenceGenerator)
	{
		this.txnSequenceGenerator = txnSequenceGenerator;
	}
}
