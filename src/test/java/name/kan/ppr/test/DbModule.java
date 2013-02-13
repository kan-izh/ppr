package name.kan.ppr.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.h2.jdbcx.JdbcDataSource;

import javax.inject.Named;
import javax.sql.DataSource;

import static com.google.inject.name.Names.named;

/**
 * @author kan
 * @since 2013-02-02 09:57
 */
public class DbModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bindConstant()
				.annotatedWith(named("jdbcUrl"))
				.to("jdbc:h2:mem:unit_test;DB_CLOSE_DELAY=-1");
	}

	@Provides
	public DataSource getDataSource(final @Named("jdbcUrl") String jdbcUrl)
	{
		final JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(jdbcUrl);
		return ds;
	}
}
