package name.kan.ppr.web.txn;

import name.kan.ppr.model.txn.TxnEntity;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import javax.inject.Inject;

/**
 * @author kan
 * @since 2013-09-06 00:31
 */
public class TxnListPage extends WebPage
{
	private static final long serialVersionUID = -5838157399836068192L;
	@Inject
	private IDataProvider<TxnEntity> dataProvider;

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new BookmarkablePageLink<Void>("homePageLink", getApplication().getHomePage()));
		final WebMarkupContainer table = new WebMarkupContainer("txnTable");
		add(table.setOutputMarkupId(true));
		final TxnDataView dataView = new TxnDataView("txns", dataProvider);
		table.add(dataView);
		add(new AjaxPagingNavigator("navigator", dataView));
	}
}
