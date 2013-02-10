package name.kan.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
* @author kan
* @since 2013-02-10 15:20
*/
public class TransactionalInterceptor implements MethodInterceptor
{
	@Inject
	private DataSource dataSource;

	@Inject
	private TransactionalConnectionProviderImpl connectionProvider;

	@Override
	public Object invoke(final MethodInvocation mi) throws Throwable
	{

		final Connection currentConnection = connectionProvider.getCurrentConnection();
		if(currentConnection != null)
			throw new UnsupportedOperationException("TODO");
		else
			return newConnection(mi);
	}

	private Object newConnection(final MethodInvocation mi) throws Throwable
	{
		final Method method = mi.getMethod();
		try(final Connection connection = dataSource.getConnection())
		{
			prepareConnection(connection, method);
			connectionProvider.setCurrentConnection(connection);

			final Object result;
			try
			{
				result = mi.proceed();
			} catch(Throwable t)
			{
				if(needsRollback(t, mi))
					connection.rollback();
				else
					connection.commit();
				throw t;
			}
			connection.commit();
			return result;
		}
		finally
		{
			connectionProvider.setCurrentConnection(null);
		}
	}

	private boolean needsRollback(final Throwable t, final MethodInvocation mi)
	{
		final Method method = mi.getMethod();
		final Transactional annotation = method.getAnnotation(Transactional.class);
		for(Class<? extends Exception> e : annotation.rollbackOn())
		{
			if(e.isAssignableFrom(t.getClass()))
				return true;
		}
		for(Class<? extends Exception> e : annotation.ignore())
		{
			if(e.isAssignableFrom(t.getClass()))
				return false;
		}
		for(Class<?> e : method.getExceptionTypes())
		{
			if(e.isAssignableFrom(t.getClass()))
				return false;
		}
		return true;
	}

	private void prepareConnection(final Connection connection, final Method method) throws SQLException
	{
		final Transactional annotation = method.getAnnotation(Transactional.class);
		connection.setAutoCommit(false);
		connection.setReadOnly(annotation.readOnly());
	}
}
