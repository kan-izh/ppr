package name.kan.ppr.model.txn;

import com.google.inject.Injector;
import name.kan.jdbc.TransactionalModule;
import name.kan.ppr.model.account.AccountEntity;
import name.kan.ppr.model.account.AccountModule;
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
public class TxnRepositoryImplTest
{
	private static final Currency GBP = Currency.getInstance("GBP");
	@Inject
	TxnRepositoryImpl impl;

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
				new AccountModule(),
				new TxnModule(),
				new TransactionalModule());
		injector.injectMembers(this);

		liquibaseWorker.setUp("name/kan/ppr/test/liquibase.xml");
	}

	@Test
	public void testCreateTxn() throws Exception
	{
		final TxnTypeEntity type = new TxnTypeEntity();
		type.setId(1000);
		final AccountEntity account = new AccountEntity();
		account.setId(2000);
		final TxnEntity entity = new TxnEntity();
		entity.setDateTime(DateTime.now());
		entity.setReference("ref");
		entity.setType(type);
		entity.setAccount(account);
		entity.setStatus(TxnStatus.COMPLETED);
		entity.setCurrency(GBP);
		entity.setGross(BigDecimal.valueOf(12.34));
		entity.setFee(BigDecimal.valueOf(1.23));
		impl.save(entity);
		try(
				final Connection connection = dataSource.getConnection();
				final Statement statement = connection.createStatement())
		{
			final ResultSet rs = statement.executeQuery("SELECT * FROM txn");
			rs.next();
			assertEquals("ref", rs.getString("ref"));
			assertThat(rs.next(), equalTo(false));
		}
	}
}
