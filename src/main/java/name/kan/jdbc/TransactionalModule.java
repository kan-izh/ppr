package name.kan.jdbc;

import com.google.inject.AbstractModule;

import java.sql.Connection;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
* @author kan
* @since 2013-02-10 18:11
*/
public class TransactionalModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(Connection.class)
				.toProvider(TransactionalConnectionProviderImpl.class);
		final TransactionalInterceptor transactionalInterceptor = new TransactionalInterceptor();
		requestInjection(transactionalInterceptor);
		bindInterceptor(any(), annotatedWith(Transactional.class),
				transactionalInterceptor);
	}

}
