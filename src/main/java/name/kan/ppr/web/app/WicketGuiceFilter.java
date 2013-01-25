package name.kan.ppr.web.app;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author kan
 * @since 2013-01-24 22:41
 */
@Singleton
public class WicketGuiceFilter extends WicketFilter
{
	@Inject
	public WicketGuiceFilter(final WebApplication application)
	{
		super(application);
	}
}
