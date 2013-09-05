package name.kan.mockito;

import com.google.inject.Module;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;

/**
 * @author kan
 * @since 2013-09-05 21:36
 */
public class MockitoWicketTester
{
	public static void initInjector(final WicketTester wicketTester, final Module... modules)
	{
		final WebApplication application = wicketTester.getApplication();
		final GuiceComponentInjector injector = new GuiceComponentInjector(application, modules);
		application.getComponentInstantiationListeners().add(injector);
	}
}
