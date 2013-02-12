package name.kan.ppr.model.txn;

import com.google.inject.Guice;
import com.google.inject.Injector;
import name.kan.jdbc.TransactionalModule;
import name.kan.ppr.test.DbModule;
import name.kan.ppr.test.LiquibaseWorker;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

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
				new TxnModule());
		injector.injectMembers(this);

		liquibaseWorker.update("name/kan/ppr/test/liquibase.xml");
	}

	@Test
	public void testObtainByName() throws Exception
	{
		final TxnType type = txnTypeRepository.obtainByName("test");
		assertEquals("test", type.getName());
	}
}
