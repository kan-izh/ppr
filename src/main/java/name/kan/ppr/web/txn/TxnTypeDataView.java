package name.kan.ppr.web.txn;

import name.kan.ppr.model.txn.TxnTypeEntity;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;

/**
* @author kan
* @since 2013-08-28 00:08
*/
public class TxnTypeDataView extends DataView<TxnTypeEntity>
{
	private static final long serialVersionUID = -8387382392391561930L;
	private final int level;
	@Inject
	private TxnTypeChildrenDataProviderFactory dataProviderFactory;

	public TxnTypeDataView(final String id, final int level, final IDataProvider<TxnTypeEntity> dataProvider)
	{
		super(id, dataProvider);
		this.level = level;
	}

	@Override
	protected void populateItem(final Item<TxnTypeEntity> item)
	{
		item.add(new Label("id", PropertyModel.of(item.getModel(), "id")));
		item.add(new Label("name", PropertyModel.of(item.getModel(), "name")));
		final IDataProvider<TxnTypeEntity> dataProvider = dataProviderFactory.create(item.getModel());
		if(level == 0)
		{
			item.add(new TxnTypeDataView("typeChildren", level + 1, dataProvider));
		}
	}

}
