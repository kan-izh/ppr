package name.kan.ppr.model.account;

import com.google.inject.name.Named;
import name.kan.jdbc.SequenceGenerator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kan
 * @since 2013-02-15 20:48
 */
public class AccountRepositoryImpl implements AccountRepository
{
	@Inject
	private Provider<Connection> connectionProvider;

	@Inject@Named("account_seq")
	private SequenceGenerator sequenceGenerator;

	@Override
	public AccountEntity obtainByName(final String name)
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

/*
	private void load(final AccountEntity account) throws SQLException
	{
		final PreparedStatement stmt = connection().prepareStatement(
				"SELECT id, name FROM account WHERE id=?");
		stmt.setLong(1, account.getId());
		final ResultSet rs = stmt.executeQuery();
		map(rs, account);
	}
*/

	private AccountEntity createNew(final String name) throws SQLException
	{
		final AccountEntity txnType = new AccountEntity();
		txnType.setId(sequenceGenerator.next());
		txnType.setName(name);
		final PreparedStatement st = connection().prepareStatement("INSERT INTO account(id, name) VALUES(?, ?)");
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
		final PreparedStatement stmt = connection().prepareStatement(
				"SELECT id, name FROM account WHERE name=?");
		stmt.setString(1, name);
		return stmt.executeQuery();
	}

	private AccountEntity map(final ResultSet rs) throws SQLException
	{
		final AccountEntity account = new AccountEntity();
		map(rs, account);
		return account;
	}

	private void map(final ResultSet rs, final AccountEntity account) throws SQLException
	{
		account.setId(rs.getLong("id"));
		account.setName(rs.getString("name"));
		final long parentId = rs.getLong("parent_id");
		if(!rs.wasNull())
		{
			final AccountEntity parentAccount = new AccountEntity();
			parentAccount.setId(parentId);
			account.setParentAccount(parentAccount);
		}
	}
}
