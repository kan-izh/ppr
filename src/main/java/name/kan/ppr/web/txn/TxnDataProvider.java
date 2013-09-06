package name.kan.ppr.web.txn;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.txn.TxnEntity;
import name.kan.ppr.model.txn.TxnRepository;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;
import java.util.Iterator;

/**
* @author kan
* @since 2013-08-28 00:09
*/
public class TxnDataProvider implements IDataProvider<TxnEntity>
{
	private final TxnRepository repository;

	private static final long serialVersionUID = -3957372751175106651L;

	@Inject
	TxnDataProvider(final TxnRepository repository)
	{
		this.repository = repository;
	}

	@Transactional
	@Override
	public Iterator<? extends TxnEntity> iterator(final long first, final long count)
	{
		return repository.findTxns(first, count).iterator();
	}

	@Transactional
	@Override
	public long size()
	{
		return repository.countTxns();
	}

	@Override
	public IModel<TxnEntity> model(final TxnEntity object)
	{
		return Model.of(object);
	}

	@Override
	public void detach()
	{
	}
}
