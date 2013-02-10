package name.kan.jdbc;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.sql.Connection;

/**
 * @author kan
 * @since 2013-02-09 23:09
 */
@Singleton
public class TransactionalConnectionProviderImpl implements Provider<Connection>
{
	private ThreadLocal<Connection> currentConnection = new ThreadLocal<>();

	@Override
	public Connection get()
	{
		final Connection connection = currentConnection.get();
		if(connection == null)
			throw new IllegalStateException("Transaction was not open. Please annotate a service method with @Transactional");
		return connection;
	}

	public void setCurrentConnection(final Connection connection)
	{
		currentConnection.set(connection);
	}

	public Connection getCurrentConnection()
	{
		return currentConnection.get();
	}
}
