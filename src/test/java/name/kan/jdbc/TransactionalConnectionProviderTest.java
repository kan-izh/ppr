package name.kan.jdbc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-02-10 13:06
 */
public class TransactionalConnectionProviderTest
{
	@Inject
	private SomeService someService;

	@Mock
	private DataSource dataSource;

	@Mock
	private Connection connection;

	@Mock
	private Connection connection2;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final Injector injector = Guice.createInjector(
				new TransactionalModule(),
				new TestModule(dataSource)
		);
		injector.injectMembers(this);
		when(dataSource.getConnection()).thenReturn(connection);
	}

	@Test
	public void transactionalMethod() throws Exception
	{
		someService.transactionalMethod();
		verify(connection).commit();
		verify(connection).close();
	}

	@Test
	public void transactionalMethodTwice() throws Exception
	{
		someService.transactionalMethod();
		verify(connection).commit();
		verify(connection).close();
		when(dataSource.getConnection()).thenReturn(connection2);
		someService.transactionalMethod();
		verify(connection2).commit();
		verify(connection2).close();
	}

	public static class SomeService
	{
		@Inject
		private Provider<Connection> connectionProvider;

		@Transactional
		public void transactionalMethod()
		{
			try
			{
				connectionProvider.get().createStatement();
			} catch(SQLException e)
			{
				throw new AssertionError(e);
			}
		}
	}

	public static class TestModule extends AbstractModule
	{
		private final DataSource dataSource;

		public TestModule(final DataSource dataSource)
		{
			this.dataSource = dataSource;
		}

		@Override
		protected void configure()
		{
			bind(DataSource.class)
					.toInstance(dataSource);
		}
	}
}
