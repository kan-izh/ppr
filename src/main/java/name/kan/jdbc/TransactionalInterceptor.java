package name.kan.jdbc;

import name.kan.guice.slf4j.InjectLogger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

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

	@InjectLogger
	private Logger logger;

	@Override
	public Object invoke(final MethodInvocation mi) throws Throwable
	{

		final Connection currentConnection = connectionProvider.getCurrentConnection();
		if(currentConnection != null)
			return supportConnection(mi, currentConnection);
		else
			return openConnection(mi);
	}

	private Object supportConnection(final MethodInvocation mi, final Connection connection) throws Throwable
	{
		logger.debug("Support connection {}", connection);
		return proceed(mi, connection);
	}

	private Object openConnection(final MethodInvocation mi) throws Throwable
	{
		try(final Connection connection = dataSource.getConnection())
		{
			logger.debug("Open connection {}", connection);
			prepareConnection(mi, connection);
			connectionProvider.setCurrentConnection(connection);

			final Object result = proceed(mi, connection);
			connection.commit();
			return result;
		}
		finally
		{
			logger.debug("Closed connection");
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
			logger.debug("Exception thrown in the connection {}: {}", connection, t.toString());
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
