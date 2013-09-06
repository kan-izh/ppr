package name.kan.ppr.model.account;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import name.kan.jdbc.PostgresSequenceGenerator;
import name.kan.jdbc.SequenceGenerator;
import name.kan.ppr.web.account.AccountDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;

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
		bind(new TypeLiteral<IDataProvider<AccountEntity>>(){})
				.to(AccountDataProvider.class);
	}
}
