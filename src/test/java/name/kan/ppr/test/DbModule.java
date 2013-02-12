package name.kan.ppr.test;

import com.google.inject.AbstractModule;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

/**
 * @author kan
 * @since 2013-02-02 09:57
 */
public class DbModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		final JdbcDataSource h2Ds = new JdbcDataSource();
		h2Ds.setURL("jdbc:h2:mem:unit_test;DB_CLOSE_DELAY=-1");
		bind(DataSource.class)
				.toInstance(h2Ds);
	}
}
