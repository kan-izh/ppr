package name.kan.sql;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author kan
 * @since 2013-02-02 12:27
 */
public class PostgresSequenceGenerator implements SequenceGenerator
{
	private Provider<Connection> connectionProvider;
	private final String sql;

	public PostgresSequenceGenerator(final String sequenceName)
	{
		sql = "SELECT nextval('" + sequenceName + "')";
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

	@Override
	public long next()
	{
		final Connection connection = getConnectionProvider().get();
		try(final Statement statement = connection.createStatement())
		{
			final ResultSet rs = statement.executeQuery(sql);
			return rs.getLong(1);
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
