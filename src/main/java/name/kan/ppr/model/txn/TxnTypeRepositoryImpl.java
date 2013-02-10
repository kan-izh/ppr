package name.kan.ppr.model.txn;

import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;
import name.kan.jdbc.SequenceGenerator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kan
 * @since 2013-02-09 22:41
 */
public class TxnTypeRepositoryImpl implements TxnTypeRepository
{
	@Inject
	private Provider<Connection> connectionProvider;

	@Inject@Named("tnx_type")
	private SequenceGenerator sequenceGenerator;

	@Transactional
	@Override
	public TxnType obtainByName(final String name)
	{
		try
		{
			final ResultSet rs = findByName(name);
			if(rs.next())
				return map(rs);
			else
				return createNew(name);
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private TxnType createNew(final String name) throws SQLException
	{
		final TxnType txnType = new TxnType();
		txnType.setId(sequenceGenerator.next());
		txnType.setName(name);
		final PreparedStatement st = connection().prepareStatement("INSERT INTO txn_type(id, name) VALUES(?, ?)");
		st.setLong(1, txnType.getId());
		st.setString(2, txnType.getName());
		return txnType;
	}

	private Connection connection()
	{
		return connectionProvider.get();
	}

	private ResultSet findByName(final String name) throws SQLException
	{
		final PreparedStatement stmt = connection().prepareStatement("SELECT id, name FROM txn_type WHERE name=?");
		stmt.setString(1, name);
		return stmt.executeQuery();
	}

	private TxnType map(final ResultSet rs) throws SQLException
	{
		final TxnType type = new TxnType();
		type.setId(rs.getLong("id"));
		type.setName(rs.getString("name"));
		return type;
	}
}
