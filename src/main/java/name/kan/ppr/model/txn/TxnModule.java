package name.kan.ppr.model.txn;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import name.kan.jdbc.PostgresSequenceGenerator;
import name.kan.jdbc.SequenceGenerator;
import name.kan.ppr.parser.JdbcPaypalParserCallback;
import name.kan.ppr.parser.PaypalParserCallback;
import name.kan.ppr.web.txn.TxnTypeChildrenDataProvider;
import name.kan.ppr.web.txn.TxnTypeChildrenDataProviderFactory;
import name.kan.ppr.web.txn.TxnTypeDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;

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
		bind(TxnRepository.class)
				.to(TxnRepositoryImpl.class);
		bind(TxnTypeRepository.class)
				.to(TxnTypeRepositoryImpl.class);
		bind(PaypalParserCallback.class)
				.to(JdbcPaypalParserCallback.class);
		for(String seqName : new String[]{
				"txn_seq",
				"txn_type_seq"
		})
		{
			bind(SequenceGenerator.class)
					.annotatedWith(named(seqName))
					.toInstance(new PostgresSequenceGenerator(seqName));
		}
		bind(new TypeLiteral<IDataProvider<TxnTypeEntity>>(){})
				.to(TxnTypeDataProvider.class);
		install(new FactoryModuleBuilder()
				.implement(new TypeLiteral<IDataProvider<TxnTypeEntity>>(){}, TxnTypeChildrenDataProvider.class)
				.build(TxnTypeChildrenDataProviderFactory.class));
	}
}
