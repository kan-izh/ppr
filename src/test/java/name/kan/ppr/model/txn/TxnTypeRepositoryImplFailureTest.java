package name.kan.ppr.model.txn;

import com.google.inject.Guice;
import com.google.inject.Injector;
import name.kan.ppr.model.account.AccountModule;
import name.kan.ppr.test.FailDbModule;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author kan
 * @since 2013-02-13 20:27
 */
public class TxnTypeRepositoryImplFailureTest
{
	@Inject
	TxnTypeRepository txnTypeRepository;

	@Before
	public void setUp() throws Exception
	{
		final Injector injector = Guice.createInjector(
				new FailDbModule(),
				new AccountModule(),
				new TxnModule());
		injector.injectMembers(this);
	}

	@Test(expected = RuntimeException.class)
	public void testObtainByName() throws Exception
	{
		txnTypeRepository.obtainByName("name");
	}

}
