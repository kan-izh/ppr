package name.kan.wicket.model;

import org.apache.wicket.model.IModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * @author kan
 * @since 2013-05-10 21:50
 */
public class MoneyModel implements IModel<String>
{
	private static final long serialVersionUID = 1782070150202739369L;

	private final Locale locale;
	private final IModel<BigDecimal> value;
	private final IModel<Currency> currency;

	public MoneyModel(final Locale locale, final IModel<BigDecimal> value, final IModel<Currency> currency)
	{
		this.locale = locale;
		this.value = value;
		this.currency = currency;
	}

	public static MoneyModel of(final Locale locale, final IModel<BigDecimal> value, final IModel<Currency> currency)
	{
		return new MoneyModel(locale, value, currency);
	}

	@Override
	public String getObject()
	{
		final BigDecimal valueObject = value.getObject();
		if(valueObject == null)
			return "";
		final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		format.setCurrency(currency.getObject());
		return format.format(valueObject);
	}

	@Override
	public void setObject(final String object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		value.detach();
		currency.detach();
	}
}
