package name.kan.ppr.model.report;

import name.kan.ppr.model.DatePeriod;
import name.kan.ppr.model.AbstractIdentifiedEntity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * @author kan
 * @since 2013-02-15 20:00
 */
public class LedgerSummaryReport extends AbstractIdentifiedEntity
{
	private static final long serialVersionUID = -6677677113192721267L;

	private DatePeriod datePeriod;
	private Currency currency;

	private BigDecimal openingBalance;
	private BigDecimal closingBalance;

	private List<LedgerSummaryGroup> groups;

	public DatePeriod getDatePeriod()
	{
		return datePeriod;
	}

	public void setDatePeriod(final DatePeriod datePeriod)
	{
		this.datePeriod = datePeriod;
	}

	public Currency getCurrency()
	{
		return currency;
	}

	public void setCurrency(final Currency currency)
	{
		this.currency = currency;
	}

	public BigDecimal getOpeningBalance()
	{
		return openingBalance;
	}

	public void setOpeningBalance(final BigDecimal openingBalance)
	{
		this.openingBalance = openingBalance;
	}

	public BigDecimal getClosingBalance()
	{
		return closingBalance;
	}

	public void setClosingBalance(final BigDecimal closingBalance)
	{
		this.closingBalance = closingBalance;
	}

	public List<LedgerSummaryGroup> getGroups()
	{
		return groups;
	}

	public void setGroups(final List<LedgerSummaryGroup> groups)
	{
		this.groups = groups;
	}
}
