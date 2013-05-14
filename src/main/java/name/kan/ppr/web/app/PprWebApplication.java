package name.kan.ppr.web.app;

import name.kan.ppr.web.main.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author kan
 * @since 2013-01-24 21:36
 */
public class PprWebApplication extends WebApplication
{
	@Override
	protected void init()
	{
		super.init();
		final GuiceComponentInjector injector = new GuiceComponentInjector(
				this,
				new PprWebApplicationModule());
		getComponentInstantiationListeners().add(injector);
	}

	@Override
	public Class<? extends Page> getHomePage()
	{
		return HomePage.class;
	}
}
