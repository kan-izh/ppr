package name.kan.ppr.web.wicket.model;

import org.apache.wicket.model.IModel;

import java.math.BigDecimal;

/**
 * @author kan
 * @since 2013-05-10 23:06
 */
public class PositiveMoneyModel implements IModel<BigDecimal>
{
	private static final long serialVersionUID = -298031732123814244L;

	private final boolean positive;
	private final IModel<BigDecimal> value;

	public PositiveMoneyModel(final boolean positive, final IModel<BigDecimal> value)
	{
		this.positive = positive;
		this.value = value;
	}

	@Override
	public BigDecimal getObject()
	{
		final BigDecimal object = value.getObject();
		if(object == null)
			return null;
		if(positive)
			if(object.compareTo(BigDecimal.ZERO) > 0)
				return object;
			else
				return null;
		else
			if(object.compareTo(BigDecimal.ZERO) < 0)
				return object.negate();
			else
				return null;
	}

	@Override
	public void setObject(final BigDecimal object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		value.detach();
	}
}
