package name.kan.ppr.parser;

import com.google.inject.Injector;
import name.kan.ppr.model.txn.TxnStatus;
import name.kan.ppr.model.txn.TxnType;
import name.kan.ppr.test.DbModule;
import name.kan.ppr.test.LiquibaseFactory;
import name.kan.ppr.test.LiquibaseModule;
import name.kan.sql.SequenceGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import javax.inject.Provider;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Currency;

import static com.google.inject.Guice.createInjector;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-02-02 01:02
 */
public class JdbcPaypalParserCallbackTest
{
	private static final Currency GBP = Currency.getInstance("GBP");
	@InjectMocks
	JdbcPaypalParserCallback callback = new JdbcPaypalParserCallback();

	@Mock
	SequenceGenerator txnSequenceGenerator;

	@Inject
	Provider<Connection> connectionProvider;

	@Inject
	LiquibaseFactory liquibaseFactory;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final Injector injector = createInjector(new DbModule(), new LiquibaseModule());
		injector.injectMembers(this);

		callback.setConnectionProvider(connectionProvider);
		liquibaseFactory.get("name/kan/ppr/test/liquibase.xml").update(null);
	}

	@Test
	public void testCreateTxn() throws Exception
	{
		final TxnType type = new TxnType();
		type.setId(1);
		when(txnSequenceGenerator.next()).thenReturn(5L);
		final long id = callback.createTxn("ref", DateTime.now(), type, TxnStatus.COMPLETED, GBP, BigDecimal.valueOf(12.34), BigDecimal.valueOf(1.23));
		assertEquals(5L, id);
		try(final Statement statement = connectionProvider.get().createStatement())
		{
			final ResultSet rs = statement.executeQuery("SELECT * FROM txn");
			rs.next();
			assertEquals(id, rs.getLong("id"));
			assertThat(rs.next(), equalTo(false));
		}
	}
}
