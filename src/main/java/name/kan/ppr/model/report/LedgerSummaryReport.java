package name.kan.ppr.model.report;

import com.google.common.collect.Lists;
import name.kan.ppr.model.AbstractIdentifiedEntity;
import name.kan.ppr.model.DatePeriod;

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

	private BigDecimal openingBalanceAmount;
	private BigDecimal closingBalanceAmount;

	private List<LedgerSummaryGroup> groups = Lists.newArrayList();

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

	public BigDecimal getOpeningBalanceAmount()
	{
		return openingBalanceAmount;
	}

	public void setOpeningBalanceAmount(final BigDecimal openingBalanceAmount)
	{
		this.openingBalanceAmount = openingBalanceAmount;
	}

	public BigDecimal getClosingBalanceAmount()
	{
		return closingBalanceAmount;
	}

	public void setClosingBalanceAmount(final BigDecimal closingBalanceAmount)
	{
		this.closingBalanceAmount = closingBalanceAmount;
	}

	public List<LedgerSummaryGroup> getGroups()
	{
		return groups;
	}
}
