package name.kan.ppr.test;

import com.google.inject.AbstractModule;
import com.google.inject.ProvisionException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author kan
 * @since 2013-02-02 09:57
 */
public class DbModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(Driver.class).to(org.h2.Driver.class);
		try
		{
			bind(Connection.class).toInstance(DriverManager.getConnection("jdbc:h2:mem:unit_test"));
		} catch(SQLException e)
		{
			throw new ProvisionException("Cannot connect", e);
		}
	}
}
