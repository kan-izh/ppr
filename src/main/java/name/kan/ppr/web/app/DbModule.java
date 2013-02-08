package name.kan.ppr.web.app;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import static com.google.inject.jndi.JndiIntegration.fromJndi;

/**
 * @author kan
 * @since 2013-02-08 18:57
 */
public class DbModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(Context.class)
				.to(InitialContext.class);
		bind(DataSource.class)
				.toProvider(fromJndi(DataSource.class, "java:comp/env/jdbc/ppr"))
				.in(Scopes.SINGLETON);
	}
}
