package name.kan.ppr.parser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-02-02 15:47
 */
public class JdbcPaypalParserCallbackFaultsTest
{
	JdbcPaypalParserCallback callback = new JdbcPaypalParserCallback();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	Provider<Connection> connectionProvider;

	@Mock
	Connection connection;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		when(connectionProvider.get()).thenReturn(connection);
		callback.setConnectionProvider(connectionProvider);
	}

	@Test
	public void testCreateTxn() throws Exception
	{
		final SQLException exception = new SQLException();
		expectedException.expect(hasProperty("cause", equalTo(exception)));
		when(connection.prepareStatement(Matchers.<String>any())).thenThrow(exception);
		callback.createTxn(null, null, null, null, null, null, null);
	}
}
