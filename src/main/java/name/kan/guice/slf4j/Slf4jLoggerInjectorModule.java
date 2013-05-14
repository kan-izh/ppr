package name.kan.guice.slf4j;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * @author kan
 * @since 2013-05-14 19:55
 */
public class Slf4jLoggerInjectorModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bindListener(Matchers.any(), new Slf4jTypeListener());
	}
}
