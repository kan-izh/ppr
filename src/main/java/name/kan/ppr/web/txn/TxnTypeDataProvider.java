package name.kan.ppr.web.txn;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.txn.TxnTypeEntity;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;
import java.util.Iterator;

/**
* @author kan
* @since 2013-08-28 00:09
*/
public class TxnTypeDataProvider implements IDataProvider<TxnTypeEntity>
{
	private final TxnTypeRepository txnTypeRepository;

	private static final long serialVersionUID = -3957372751175106651L;

	@Inject
	TxnTypeDataProvider(final TxnTypeRepository txnTypeRepository)
	{
		this.txnTypeRepository = txnTypeRepository;
	}

	@Transactional
	@Override
	public Iterator<? extends TxnTypeEntity> iterator(final long first, final long count)
	{
		return txnTypeRepository.findTopLevelTypes().iterator();
	}

	@Transactional
	@Override
	public long size()
	{
		return txnTypeRepository.findTopLevelTypes().size();
	}

	@Override
	public IModel<TxnTypeEntity> model(final TxnTypeEntity object)
	{
		return Model.of(object);
	}

	@Override
	public void detach()
	{
	}
}
