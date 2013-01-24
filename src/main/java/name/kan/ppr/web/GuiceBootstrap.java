package name.kan.ppr.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * @author kan
 * @since 2013-01-24 21:56
 */

public class GuiceBootstrap extends GuiceServletContextListener
{

	@Override
	protected Injector getInjector()
	{
		return Guice.createInjector(
				new WebModule()
		);
	}
}