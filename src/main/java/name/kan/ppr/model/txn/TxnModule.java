package name.kan.ppr.model.txn;

import com.google.inject.AbstractModule;
import name.kan.jdbc.PostgresSequenceGenerator;
import name.kan.jdbc.SequenceGenerator;

import static com.google.inject.name.Names.named;

/**
 * @author kan
 * @since 2013-02-09 23:03
 */
public class TxnModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(TxnTypeRepository.class)
				.to(TxnTypeRepositoryImpl.class);
		bind(SequenceGenerator.class)
				.annotatedWith(named("txn_type"))
				.toInstance(new PostgresSequenceGenerator("txn_type_seq"));

	}
}
