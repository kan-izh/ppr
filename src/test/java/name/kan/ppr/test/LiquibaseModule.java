package name.kan.ppr.test;

import com.google.inject.AbstractModule;
import com.google.inject.ProvisionException;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;

/**
 * @author kan
 * @since 2013-02-02 10:22
 */
public class LiquibaseModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(LiquibaseFactory.class).to(LiquibaseFactoryImpl.class);
	}

	public static class LiquibaseFactoryImpl implements LiquibaseFactory
	{
		@Inject
		private Provider<Connection> connectionProvider;

		@Override
		public Liquibase get(final String changeLogFile)
		{
			final JdbcConnection connection = new JdbcConnection(connectionProvider.get());
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
}
