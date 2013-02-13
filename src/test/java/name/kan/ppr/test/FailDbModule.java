package name.kan.ppr.test;

import com.google.inject.AbstractModule;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

/**
* @author kan
* @since 2013-02-13 20:38
*/
public class FailDbModule extends AbstractModule
{
	@Mock
	Connection connection;

	public FailDbModule() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		when(connection.prepareStatement(Matchers.<String>any()))
				.thenThrow(new SQLException("boom"));
	}

	@Override
	protected void configure()
	{
		bind(Connection.class)
				.toInstance(connection);
	}
}
