package name.kan.ppr.model.txn;

import com.google.common.collect.Lists;
import com.google.inject.name.Named;
import name.kan.jdbc.SequenceGenerator;
import name.kan.jdbc.Transactional;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author kan
 * @since 2013-02-09 22:41
 */
public class TxnTypeRepositoryImpl implements TxnTypeRepository
{
	@Inject
	private Provider<Connection> connectionProvider;

	@Inject@Named("txn_type_seq")
	private SequenceGenerator sequenceGenerator;

	@Transactional(readOnly = false)
	@Override
	public TxnTypeEntity obtainByName(final String name)
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

	@Override
	public List<TxnTypeEntity> findTopLevelTypes()
	{
		try
		{
			final List<TxnTypeEntity> result = Lists.newArrayList();
			final PreparedStatement ps = connection().prepareStatement(
					"SELECT *" +
							" FROM txn_type" +
							" WHERE parent_id IS NULL" +
							" ORDER BY ordinal, name");
			for(final ResultSet rs = ps.executeQuery(); rs.next(); )
			{
				final TxnTypeEntity type = map(rs);
				result.add(type);
			}
			return result;
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private TxnTypeEntity createNew(final String name) throws SQLException
	{
		final TxnTypeEntity txnType = new TxnTypeEntity();
		txnType.setId(sequenceGenerator.next());
		txnType.setName(name);
		final PreparedStatement st = connection().prepareStatement("INSERT INTO txn_type(id, name) VALUES(?, ?)");
		st.setLong(1, txnType.getId());
		st.setString(2, txnType.getName());
		st.execute();
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

	private TxnTypeEntity map(final ResultSet rs) throws SQLException
	{
		final TxnTypeEntity type = new TxnTypeEntity();
		type.setId(rs.getLong("id"));
		type.setName(rs.getString("name"));
		return type;
	}
}
