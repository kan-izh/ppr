package name.kan.ppr.web.txn;

import name.kan.ppr.model.txn.TxnTypeEntity;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author kan
 * @since 2013-05-16 23:58
 */
public class TxnTypeListPage extends WebPage
{
	private static final long serialVersionUID = -5120090888578873075L;
	@Inject
	private Provider<IDataProvider<TxnTypeEntity>> dataProviderProvider;

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new BookmarkablePageLink<Void>("homePageLink", getApplication().getHomePage()));
		final IDataProvider<TxnTypeEntity> dataProvider = dataProviderProvider.get();
		add(new TxnTypeDataView("types", 0, dataProvider));
	}
}
