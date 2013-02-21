package name.kan.ppr.model.txn;

import name.kan.ppr.model.AbstractIdentifiedEntity;
import name.kan.ppr.model.account.AccountEntity;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-02-15 20:58
 */
public class TxnEntity extends AbstractIdentifiedEntity
{
	private static final long serialVersionUID = -7500819159472461414L;
	private AccountEntity account;
	private String reference;
	private DateTime dateTime;
	private TxnTypeEntity type;
	private TxnStatus status;
	private Currency currency;
	private BigDecimal gross;
	private BigDecimal fee;

	public AccountEntity getAccount()
	{
		return account;
	}

	public void setAccount(final AccountEntity account)
	{
		this.account = account;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(final String reference)
	{
		this.reference = reference;
	}

	public DateTime getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(final DateTime dateTime)
	{
		this.dateTime = dateTime;
	}

	public TxnTypeEntity getType()
	{
		return type;
	}

	public void setType(final TxnTypeEntity type)
	{
		this.type = type;
	}

	public TxnStatus getStatus()
	{
		return status;
	}

	public void setStatus(final TxnStatus status)
	{
		this.status = status;
	}

	public Currency getCurrency()
	{
		return currency;
	}

	public void setCurrency(final Currency currency)
	{
		this.currency = currency;
	}

	public BigDecimal getGross()
	{
		return gross;
	}

	public void setGross(final BigDecimal gross)
	{
		this.gross = gross;
	}

	public BigDecimal getFee()
	{
		return fee;
	}

	public void setFee(final BigDecimal fee)
	{
		this.fee = fee;
	}
}
