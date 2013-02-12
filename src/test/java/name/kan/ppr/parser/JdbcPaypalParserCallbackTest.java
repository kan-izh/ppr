package name.kan.ppr.parser;

import com.google.inject.Injector;
import name.kan.jdbc.TransactionalModule;
import name.kan.ppr.model.txn.TxnModule;
import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnType;
import name.kan.ppr.test.DbModule;
import name.kan.ppr.test.LiquibaseWorker;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Currency;

import static com.google.inject.Guice.createInjector;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author kan
 * @since 2013-02-02 01:02
 */
public class JdbcPaypalParserCallbackTest
{
	private static final Currency GBP = Currency.getInstance("GBP");
	@Inject
	JdbcPaypalParserCallback callback;

	@Inject
	Provider<Connection> connectionProvider;

	@Inject
	LiquibaseWorker liquibaseWorker;

	@Inject
	DataSource dataSource;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final Injector injector = createInjector(
				new DbModule(),
				new TxnModule(),
				new TransactionalModule());
		injector.injectMembers(this);

		liquibaseWorker.update("name/kan/ppr/test/liquibase.xml");
	}

	@Test
	public void testCreateTxn() throws Exception
	{
		final TxnType type = new TxnType();
		type.setId(1000);
		final long id = callback.createTxn("ref", DateTime.now(), type, TxnStatus.COMPLETED, GBP, BigDecimal.valueOf(12.34), BigDecimal.valueOf(1.23));
		try(
				final Connection connection = dataSource.getConnection();
				final Statement statement = connection.createStatement())
		{
			final ResultSet rs = statement.executeQuery("SELECT * FROM txn");
			rs.next();
			assertEquals(id, rs.getLong("id"));
			assertThat(rs.next(), equalTo(false));
		}
	}
}
