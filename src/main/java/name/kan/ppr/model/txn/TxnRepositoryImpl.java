package name.kan.ppr.model.txn;

import com.google.inject.name.Named;
import name.kan.jdbc.SequenceGenerator;
import name.kan.jdbc.Transactional;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
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
			statement.setDate(3, new Date(entity.getDateTime().getMillis()), UTC_CALENDAR);
			statement.setLong(4, entity.getType().getId());
			statement.setLong(5, entity.getAccount().getId());
			statement.setInt(6, entity.getStatus().ordinal());
			statement.setInt(7, entity.getCurrency().getNumericCode());
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
}
