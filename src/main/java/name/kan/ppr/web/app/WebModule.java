package name.kan.ppr.web.app;

import com.google.inject.servlet.ServletModule;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kan
 * @since 2013-01-24 21:57
 */
public class WebModule extends ServletModule
{

	@Override
	protected void configureServlets() {
		bind(WebApplication.class).to(PprWebApplication.class);

		Map<String, String> params = new HashMap<String, String>();
		params.put(WicketFilter.FILTER_MAPPING_PARAM, "/*");

		filter("/*").through(WicketGuiceFilter.class, params);
	}
}
