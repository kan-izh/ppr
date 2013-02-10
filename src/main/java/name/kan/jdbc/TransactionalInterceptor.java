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
			return openConnection(mi);
	}

	private Object openConnection(final MethodInvocation mi) throws Throwable
	{
		try(final Connection connection = dataSource.getConnection())
		{
			prepareConnection(mi, connection);
			connectionProvider.setCurrentConnection(connection);

			final Object result = proceed(mi, connection);
			connection.commit();
			return result;
		}
		finally
		{
			connectionProvider.setCurrentConnection(null);
		}
	}

	private Object proceed(final MethodInvocation mi, final Connection connection) throws Throwable
	{
		try
		{
			return mi.proceed();
		} catch(Throwable t)
		{
			processException(mi, connection, t);
			throw t;
		}
	}

	private void processException(final MethodInvocation mi, final Connection connection, final Throwable t) throws SQLException
	{
		if(needsRollback(t, mi))
			connection.rollback();
		else
			connection.commit();
	}

	private boolean needsRollback(final Throwable t, final MethodInvocation mi)
	{
		final Method method = mi.getMethod();
		final Transactional annotation = method.getAnnotation(Transactional.class);
		for(final Class<? extends Exception> e : annotation.ignore())
		{
			if(e.isAssignableFrom(t.getClass()))
				return false;
		}
		for(final Class<? extends Exception> e : annotation.rollbackOn())
		{
			if(e.isAssignableFrom(t.getClass()))
				return true;
		}
		for(final Class<?> e : method.getExceptionTypes())
		{
			if(e.isAssignableFrom(t.getClass()))
				return false;
		}
		return true;
	}

	private void prepareConnection(final MethodInvocation mi, final Connection connection) throws SQLException
	{
		final Transactional annotation = mi.getMethod().getAnnotation(Transactional.class);
		connection.setAutoCommit(false);
		connection.setReadOnly(annotation.readOnly());
	}
}
