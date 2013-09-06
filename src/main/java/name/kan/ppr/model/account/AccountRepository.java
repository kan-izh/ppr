package name.kan.ppr.model.account;

import java.util.List;

/**
 * @author kan
 * @since 2013-02-15 20:33
 */
public interface AccountRepository
{
	AccountEntity obtainByName(String name);

	List<AccountEntity> findAccounts();
}
