package name.kan.ppr.web.account;

import name.kan.ppr.model.account.AccountEntity;
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
		add(new AccountDataView("accounts", dataProvider));
	}
}
