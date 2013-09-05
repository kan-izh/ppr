package name.kan.ppr.web.txn;

import name.kan.ppr.model.txn.TxnTypeEntity;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * @author kan
 * @since 2013-09-05 19:57
 */
public interface TxnTypeChildrenDataProviderFactory
{
	IDataProvider<TxnTypeEntity> create(IModel<TxnTypeEntity> parent);
}
