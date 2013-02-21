package name.kan.ppr.model.account;

import name.kan.ppr.model.AbstractNamedEntity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kan
 * @since 2013-02-15 20:20
 */
@Entity
@Table(name = "account")
public class AccountEntity extends AbstractNamedEntity
{
	private static final long serialVersionUID = 3561523912478327031L;

	private @Nullable AccountEntity parentAccount;

	@Nullable public AccountEntity getParentAccount()
	{
		return parentAccount;
	}

	public void setParentAccount(@Nullable final AccountEntity parentAccount)
	{
		this.parentAccount = parentAccount;
	}
}
