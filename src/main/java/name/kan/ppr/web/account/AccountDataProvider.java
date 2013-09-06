package name.kan.ppr.web.account;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.account.AccountEntity;
import name.kan.ppr.model.account.AccountRepository;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;
import java.util.Iterator;

/**
* @author kan
* @since 2013-08-28 00:09
*/
public class AccountDataProvider implements IDataProvider<AccountEntity>
{
	private final AccountRepository repository;

	private static final long serialVersionUID = -3957372751175106651L;

	@Inject
	AccountDataProvider(final AccountRepository repository)
	{
		this.repository = repository;
	}

	@Transactional
	@Override
	public Iterator<? extends AccountEntity> iterator(final long first, final long count)
	{
		return repository.findAccounts().iterator();
	}

	@Transactional
	@Override
	public long size()
	{
		return repository.findAccounts().size();
	}

	@Override
	public IModel<AccountEntity> model(final AccountEntity object)
	{
		return Model.of(object);
	}

	@Override
	public void detach()
	{
	}
}
