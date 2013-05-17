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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

	private final ConcurrentMap<Method, Map<Class<? extends Throwable>, Boolean>> needsRollbackCache = new ConcurrentHashMap<>();

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
			if(logger.isDebugEnabled())
				logger.debug("Closed connection {}", connectionProvider.getCurrentConnection());
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
		try
		{
			if(needsRollback(t, mi))
				connection.rollback();
			else
				connection.commit();
		} catch(SQLException e)
		{
			logger.error("Cannot process exception", e);
		}
	}

	private boolean needsRollback(final Throwable t, final MethodInvocation mi)
	{
		return needsRollbackCached(mi.getMethod(), t.getClass());
	}

	private boolean needsRollbackCached(final Method method, final Class<? extends Throwable> cls)
	{
		Map<Class<? extends Throwable>, Boolean> classToResult = needsRollbackCache.get(method);
		if(classToResult == null)
		{
			needsRollbackCache.putIfAbsent(method, new ConcurrentHashMap<Class<? extends Throwable>, Boolean>());
			classToResult = needsRollbackCache.get(method);
		}
		Boolean result = classToResult.get(cls);
		if(result == null)
		{
			result = needsRollback(method, cls);
			classToResult.put(cls, needsRollback(method, cls));
		}
		return result;
	}

	private boolean needsRollback(final Method method, final Class<? extends Throwable> cls)
	{
		final Transactional annotation = method.getAnnotation(Transactional.class);
		for(final Class<? extends Exception> e : annotation.ignore())
		{
			if(e.isAssignableFrom(cls))
				return false;
		}
		for(final Class<? extends Exception> e : annotation.rollbackOn())
		{
			if(e.isAssignableFrom(cls))
				return true;
		}
		for(final Class<?> e : method.getExceptionTypes())
		{
			if(e.isAssignableFrom(cls))
				return false;
		}
		return true;
	}

	private void prepareConnection(final MethodInvocation mi, final Connection connection) throws SQLException
	{
		final Transactional annotation = mi.getMethod().getAnnotation(Transactional.class);
		if(connection.getAutoCommit())
			connection.setAutoCommit(false);
		connection.rollback();
		connection.setReadOnly(annotation.readOnly());
	}
}
