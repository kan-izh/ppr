package name.kan.ppr.web.txn;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import name.kan.junit.PreviewWriter;
import name.kan.mockito.MockitoWicketTester;
import name.kan.ppr.model.txn.TxnTypeEntity;
import org.apache.wicket.markup.repeater.data.EmptyDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-09-05 21:00
 */
public class TxnTypeListPageTest
{
	@Rule
	public PreviewWriter writer = new PreviewWriter("pages");
	final WicketTester tester = new WicketTester();
	final List<TxnTypeEntity> entities = new ArrayList<>();
	@Mock
	TxnTypeChildrenDataProviderFactory providerFactory;
	private int id;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		MockitoWicketTester.initInjector(tester, new AbstractModule()
		{
			@Override
			protected void configure()
			{
				final ListDataProvider<TxnTypeEntity> provider = new ListDataProvider<>(entities);
				bind(new TypeLiteral<IDataProvider<TxnTypeEntity>>()
				{
				}).toInstance(provider);
				bind(TxnTypeChildrenDataProviderFactory.class).toInstance(providerFactory);
			}
		});
		when(providerFactory.create(Matchers.<IModel<TxnTypeEntity>>any()))
				.thenReturn(EmptyDataProvider.<TxnTypeEntity>getInstance());
	}

	@Test
	public void render() throws Exception
	{
		final TxnTypeEntity entity1 = newTxnType("Account 1");
		final TxnTypeEntity entity2 = newTxnType("Account 2");
		entities.add(entity1);
		entities.add(entity2);
		when(providerFactory.create(Model.of(entity1)))
				.thenReturn(new ListDataProvider<>(Arrays.asList(
						newTxnType("SubAccount 1.1")
				)));
		tester.startPage(TxnTypeListPage.class);
		writer.write(tester.getLastResponseAsString());
	}

	private TxnTypeEntity newTxnType(final String name)
	{
		final TxnTypeEntity entity = new TxnTypeEntity();
		entity.setId(++id);
		entity.setName(name);
		return entity;
	}
}
