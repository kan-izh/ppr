package name.kan.ppr.test;

import com.google.inject.AbstractModule;

/**
* @author kan
* @since 2013-02-19 00:20
*/
public class StaticInjectModule extends AbstractModule
{

	private final Class<?> classes[];

	public StaticInjectModule(final Class<?>... classes)
	{
		this.classes = classes;
	}

	@Override
	protected void configure()
	{
		requestStaticInjection(classes);
	}
}
