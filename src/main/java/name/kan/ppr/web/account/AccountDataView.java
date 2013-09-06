package name.kan.ppr.web.account;

import name.kan.ppr.model.account.AccountEntity;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;

/**
* @author kan
* @since 2013-08-28 00:08
*/
public class AccountDataView extends DataView<AccountEntity>
{
	private static final long serialVersionUID = -8387382392391561930L;

	public AccountDataView(final String id, final IDataProvider<AccountEntity> dataProvider)
	{
		super(id, dataProvider, 20);
	}

	@Override
	protected void populateItem(final Item<AccountEntity> item)
	{
		item.add(new Label("id", PropertyModel.of(item.getModel(), "id")));
		item.add(new Label("name", PropertyModel.of(item.getModel(), "name")));
	}

}
