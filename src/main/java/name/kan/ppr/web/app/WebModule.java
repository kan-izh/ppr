package name.kan.ppr.web.app;

import com.google.common.collect.ImmutableMap;
import com.google.inject.servlet.ServletModule;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

/**
 * @author kan
 * @since 2013-01-24 21:57
 */
public class WebModule extends ServletModule
{
	private static final String WICKET_PATTERN = "/*";

	@Override
	protected void configureServlets() {
		bind(WebApplication.class).to(PprWebApplication.class);

		filter(WICKET_PATTERN).through(WicketGuiceFilter.class, ImmutableMap.of(
				WicketFilter.FILTER_MAPPING_PARAM, WICKET_PATTERN
		));
	}
}
