package name.kan.ppr.model.txn;

import com.google.inject.name.Named;
import name.kan.jdbc.SequenceGenerator;
import name.kan.jdbc.Transactional;
import name.kan.ppr.model.account.AccountEntity;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author kan
 * @since 2013-02-16 00:23
 */
public class TxnRepositoryImpl implements TxnRepository
{
	private static final String INSERT_TXN_SQL =
			"INSERT INTO txn(\n" +
					"            id, ref, date_time, type_id, account_id, status, currency, gross, fee, credit)\n" +
					"    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

	@Inject
	private Provider<Connection> connectionProvider;

	@Inject
	@Named("txn_seq")
	private SequenceGenerator txnSequenceGenerator;

	@Transactional(readOnly = false)
	@Override
	public void save(final TxnEntity entity)
	{
		final Connection connection = connectionProvider.get();
		try(final PreparedStatement statement = connection.prepareStatement(INSERT_TXN_SQL))
		{
			final long id = txnSequenceGenerator.next();
			statement.setLong(1, id);
			statement.setString(2, entity.getReference());
			statement.setTimestamp(3, new Timestamp(entity.getDateTime().getMillis()), UTC_CALENDAR);
			statement.setLong(4, entity.getType().getId());
			statement.setLong(5, entity.getAccount().getId());
			statement.setInt(6, entity.getStatus().ordinal());
			CurrencyConverter.INSTANCE.set(statement, 7, entity.getCurrency());
			statement.setBigDecimal(8, entity.getGross());
			statement.setBigDecimal(9, entity.getFee());
			statement.setBoolean(10, entity.isCredit());
			statement.execute();
			entity.setId(id);
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<TxnEntity> findTxns(final long first, final long count)
	{
		final Connection connection = connectionProvider.get();
		try(final PreparedStatement statement = connection.prepareStatement(
				"SELECT t.ref, t.date_time, t.account_id, a.name as account_name, t.type_id, tt.name as type_name, t.status, t.currency, t.gross, t.fee, t.credit"
						+ "		FROM txn t"
						+ "		INNER JOIN account a ON (a.id = t.account_id)"
						+ "		INNER JOIN txn_type tt ON (tt.id = t.type_id)"
						+ "	ORDER BY t.date_time, lower(t.ref), t.id"
						+ "	LIMIT ? OFFSET ?"
		))
		{
			statement.setLong(1, count);
			statement.setLong(2, first);
			final ArrayList<TxnEntity> list = new ArrayList<>();
			for(final ResultSet rs = statement.executeQuery(); rs.next();)
			{
				list.add(map(rs));
			}
			return list;
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public long countTxns()
	{
		final Connection connection = connectionProvider.get();
		try
		{
			final PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM txn");
			final ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getLong(1);
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private TxnEntity map(final ResultSet rs) throws SQLException
	{
		final TxnEntity e = new TxnEntity();
		e.setReference(rs.getString("ref"));
		e.setDateTime(new DateTime(rs.getTimestamp("date_time", UTC_CALENDAR)));
		e.setStatus(TxnStatus.values()[rs.getInt("status")]);
		e.setCurrency(CurrencyConverter.INSTANCE.get(rs, "currency"));
		e.setGross(rs.getBigDecimal("gross"));
		e.setFee(rs.getBigDecimal("fee"));
		e.setCredit(rs.getBoolean("credit"));
		final AccountEntity account = new AccountEntity();
		account.setId(rs.getLong("account_id"));
		account.setName(rs.getString("account_name"));
		e.setAccount(account);
		final TxnTypeEntity type = new TxnTypeEntity();
		type.setId(rs.getLong("type_id"));
		type.setName(rs.getString("type_name"));
		e.setType(type);
		return e;
	}

	private static enum CurrencyConverter
	{
		INSTANCE;
		private final Map<Integer, Currency> codeMap = new HashMap<>();

		CurrencyConverter()
		{
			for(final Currency currency : Currency.getAvailableCurrencies())
			{
				codeMap.put(currency.getNumericCode(), currency);
			}
		}

		public Currency get(final ResultSet rs, final String columnLabel) throws SQLException
		{
			final int code = rs.getInt(columnLabel);
			return codeMap.get(code);
		}

		public void set(final PreparedStatement statement, final int index, final Currency currency) throws SQLException
		{
			statement.setInt(index, currency.getNumericCode());
		}
	}
}
