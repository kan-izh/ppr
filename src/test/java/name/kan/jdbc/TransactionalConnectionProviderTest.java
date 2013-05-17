package name.kan.jdbc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import name.kan.guice.slf4j.Slf4jLoggerInjectorModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.io.EOFException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-02-10 13:06
 */
public class TransactionalConnectionProviderTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

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
				new Slf4jLoggerInjectorModule(),
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

	@Test
	public void notAnnotated() throws Exception
	{
		expectedException.expectMessage(containsString("Transaction was not open"));
		someService.notAnnotated();
	}

	@Test
	public void throwsUnexpectedException() throws Exception
	{
		try
		{
			someService.throwsUnexpectedException();
		}
		catch(RuntimeException ignore){}
		verify(connection, never()).commit();
		verify(connection).close();
	}

	@Test
	public void throwsDeclaredException() throws Exception
	{
		try
		{
			someService.throwsDeclaredException();
		}
		catch(EOFException ignore){}
		verify(connection).commit();
		verify(connection).close();
	}

	@Test
	public void throwsIgnoredException() throws Exception
	{
		try
		{
			someService.throwsIgnoredException();
		}
		catch(ArithmeticException ignore){}
		verify(connection).commit();
		verify(connection).close();
	}

	@Test
	public void throwsAnError() throws Exception
	{
		try
		{
			someService.throwsAnError();
		}
		catch(ClassFormatError ignore){}
		verify(connection, never()).commit();
		verify(connection).close();
	}

	@Test
	public void testRecursive() throws Exception
	{
		someService.recursive(5);
		verify(connection, times(1)).commit();
		verify(connection, times(1)).close();
	}

	public static class SomeService
	{
		@Inject
		private Provider<Connection> connectionProvider;

		@Inject
		private SomeService self;

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

		@Transactional
		public void recursive(int i)
		{
			if(i == 0)
				return;
			try
			{
				connectionProvider.get().createStatement();
			} catch(SQLException e)
			{
				throw new AssertionError(e);
			}
			recursive(i - 1);
		}
		public void notAnnotated()
		{
			try
			{
				connectionProvider.get().createStatement();
			} catch(SQLException e)
			{
				throw new AssertionError(e);
			}
		}

		@Transactional
		public void throwsUnexpectedException()
		{
			throw new RuntimeException("Boooom!");
		}

		@Transactional
		public void throwsDeclaredException() throws EOFException
		{
			throw new EOFException("Boooom!");
		}

		@Transactional(ignore = ArithmeticException.class)
		public void throwsIgnoredException()
		{
			throw new ArithmeticException("Boooom!");
		}

		@Transactional(rollbackOn = ArithmeticException.class)
		public void throwsAnError()
		{
			throw new ClassFormatError("Boooom!");
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
