package name.kan.ppr.web.txn;

import name.kan.ppr.model.txn.TxnEntity;
import name.kan.wicket.model.MoneyModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.math.BigDecimal;
import java.util.Currency;

/**
* @author kan
* @since 2013-08-28 00:08
*/
public class TxnDataView extends DataView<TxnEntity>
{
	private static final long serialVersionUID = -8387382392391561930L;

	public TxnDataView(final String id, final IDataProvider<TxnEntity> dataProvider)
	{
		super(id, dataProvider, 20);
	}

	@Override
	protected void populateItem(final Item<TxnEntity> item)
	{
		final IModel<TxnEntity> model = item.getModel();
		item.add(new Label("reference", PropertyModel.of(model, "reference")));
		item.add(new Label("dateTime", PropertyModel.of(model, "dateTime")));
		item.add(new Label("account.name", PropertyModel.of(model, "account.name")));
		item.add(new Label("type.name", PropertyModel.of(model, "type.name")));
		item.add(new Label("status", PropertyModel.of(model, "status")));
		final IModel<Currency> currency = PropertyModel.of(model, "currency");
		item.add(new Label("gross", MoneyModel.of(
				getLocale(),
				PropertyModel.<BigDecimal>of(model, "gross"),
				currency)));
		item.add(new Label("fee", MoneyModel.of(
				getLocale(),
				PropertyModel.<BigDecimal>of(model, "fee"),
				currency)));
		item.add(new Label("credit", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1491769838248309471L;

			@Override
			public String getObject()
			{
				return model.getObject().isCredit() ? "Credit" : "Debit";
			}
		}));
	}

}
