package name.kan.ppr.model.report;

import name.kan.ppr.model.AbstractNamedEntity;

import java.math.BigDecimal;

/**
 * @author kan
 * @since 2013-02-15 20:11
 */
public class LedgerSummaryLine extends AbstractNamedEntity
{
	private static final long serialVersionUID = -8913264695140969488L;
	private BigDecimal gross;
	private BigDecimal fee;

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
