package name.kan.ppr.web.account;

import name.kan.ppr.model.account.AccountEntity;

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
public class AccountListPage extends WebPage
{
	private static final long serialVersionUID = -5838157399836068192L;
	@Inject
	private IDataProvider<AccountEntity> dataProvider;

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new BookmarkablePageLink<Void>("homePageLink", getApplication().getHomePage()));
		final WebMarkupContainer accountTable = new WebMarkupContainer("accountTable");
		add(accountTable.setOutputMarkupId(true));
		final AccountDataView accountDataView = new AccountDataView("accounts", dataProvider);
		accountTable.add(accountDataView);
		add(new AjaxPagingNavigator("navigator", accountDataView));
	}
}
