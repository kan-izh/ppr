package name.kan.ppr.model.account;

import com.google.inject.AbstractModule;
import name.kan.jdbc.PostgresSequenceGenerator;
import name.kan.jdbc.SequenceGenerator;

import static com.google.inject.name.Names.named;

/**
 * @author kan
 * @since 2013-02-15 20:50
 */
public class AccountModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(AccountRepository.class)
				.to(AccountRepositoryImpl.class);
		bind(SequenceGenerator.class)
				.annotatedWith(named("account_seq"))
				.toInstance(new PostgresSequenceGenerator("account_seq"));
	}
}
