package name.kan.sql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-02-02 12:59
 */
public class PostgresSequenceGeneratorTest
{
	private static final String SEQ_NAME = "test_seq";
	@InjectMocks
	private PostgresSequenceGenerator generator = new PostgresSequenceGenerator(SEQ_NAME);

	@Mock
	Provider<Connection> connectionProvider;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	Connection connection;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		when(connectionProvider.get()).thenReturn(connection);
	}

	@Test
	public void testNext() throws Exception
	{
		final String sql = String.format("SELECT nextval('%s')", SEQ_NAME);
		final ResultSet rs = connection.createStatement().executeQuery(sql);
		when(rs.getLong(1)).thenReturn(42L);
		final long next = generator.next();
		assertEquals(42L, next);
	}

	@Test(expected = RuntimeException.class)
	public void testSQLException() throws Exception
	{
		when(connection.createStatement()).thenThrow(new SQLException());
		generator.next();
	}
}
