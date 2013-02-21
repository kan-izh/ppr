package name.kan.ppr.model.txn;

import com.google.inject.Guice;
import com.google.inject.Injector;
import name.kan.jdbc.TransactionalModule;
import name.kan.ppr.model.account.AccountModule;
import name.kan.ppr.test.DbModule;
import name.kan.ppr.test.LiquibaseWorker;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author kan
 * @since 2013-02-10 23:40
 */
public class TxnTypeRepositoryImplTest
{
	@Inject
	private TxnTypeRepository txnTypeRepository;
	@Inject
	private LiquibaseWorker liquibaseWorker;

	@Before
	public void setUp() throws Exception
	{
		final Injector injector = Guice.createInjector(
				new DbModule(),
				new TransactionalModule(),
				new AccountModule(),
				new TxnModule());
		injector.injectMembers(this);

		liquibaseWorker.setUp("name/kan/ppr/test/liquibase.xml");
	}

	@Test
	public void testObtainByName() throws Exception
	{
		final TxnTypeEntity type = txnTypeRepository.obtainByName("test");
		assertEquals("test", type.getName());
	}

	@Test
	public void testObtainByNameSameName() throws Exception
	{
		final TxnTypeEntity type = txnTypeRepository.obtainByName("test");
		final TxnTypeEntity type2 = txnTypeRepository.obtainByName("test");
		assertEquals(type, type2);
	}
	@Test
	public void testObtainByNameDifferentName() throws Exception
	{
		final TxnTypeEntity type = txnTypeRepository.obtainByName("test");
		final TxnTypeEntity type2 = txnTypeRepository.obtainByName("test2");
		assertThat(type, not(equalTo(type2)));
	}

	@Test
	public void testSqlFailure() throws Exception
	{

	}
}
