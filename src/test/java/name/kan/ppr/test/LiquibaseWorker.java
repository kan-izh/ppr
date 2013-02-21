package name.kan.ppr.test;

import com.google.inject.ProvisionException;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import name.kan.jdbc.Transactional;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;

/**
* @author kan
* @since 2013-02-11 20:41
*/
public class LiquibaseWorker
{
	@Inject
	private Provider<Connection> connectionProvider;

	@Transactional(readOnly = false)
	public void setUp(final String changeLogFile) throws LiquibaseException
	{
		final Liquibase liquibase = getLiquibase(changeLogFile);
		liquibase.dropAll();
		liquibase.update(null);
	}

	private Liquibase getLiquibase(final String changeLogFile)
	{
		final DatabaseConnection connection = new JdbcConnection(connectionProvider.get());
		final ResourceAccessor resourceAccessor = new CompositeResourceAccessor(
				new ClassLoaderResourceAccessor(getClass().getClassLoader()),
				new FileSystemResourceAccessor()
		);
		try
		{
			return new Liquibase(changeLogFile, resourceAccessor, connection);
		} catch(LiquibaseException e)
		{
			throw new ProvisionException("Cannot create liquibase for " + changeLogFile, e);
		}
	}
}
