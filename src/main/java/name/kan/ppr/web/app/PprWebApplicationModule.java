package name.kan.ppr.web.app;

import com.google.inject.AbstractModule;
import name.kan.guice.slf4j.Slf4jLoggerInjectorModule;
import name.kan.jdbc.TransactionalModule;
import name.kan.ppr.model.account.AccountModule;
import name.kan.ppr.model.txn.TxnModule;

/**
 * @author kan
 * @since 2013-05-13 22:37
 */
public class PprWebApplicationModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		install(new Slf4jLoggerInjectorModule());
		install(new TransactionalModule());
		install(new TxnModule());
		install(new AccountModule());
		install(new DbModule());
	}
}
