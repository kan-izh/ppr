package name.kan.ppr.web.wicket.model;

import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author kan
 * @since 2013-05-10 22:03
 */
public class MoneyModelTest
{
	@Mock IModel<BigDecimal> value;
	@Mock IModel<Currency> currency;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDetach() throws Exception
	{
		final MoneyModel model = MoneyModel.of(Locale.UK, value, currency);
		model.detach();
		verify(value).detach();
		verify(currency).detach();
	}

	@Test
	public void testGetPound() throws Exception
	{
		when(value.getObject()).thenReturn(new BigDecimal("12345.678"));
		when(currency.getObject()).thenReturn(Currency.getInstance("GBP"));

		final MoneyModel model = MoneyModel.of(Locale.UK, value, currency);

		assertEquals("£12,345.68", model.getObject());
	}

	@Test
	public void testGetRouble() throws Exception
	{
		when(value.getObject()).thenReturn(new BigDecimal("12345.67"));
		when(currency.getObject()).thenReturn(Currency.getInstance("RUB"));

		final MoneyModel model = MoneyModel.of(new Locale("ru", "RU"), value, currency);

		assertEquals("12 345,67 руб.", model.getObject());
	}

	@Test
	public void testNullValue() throws Exception
	{
		when(currency.getObject()).thenReturn(Currency.getInstance("RUB"));

		final MoneyModel model = MoneyModel.of(Locale.UK, value, currency);

		assertEquals("", model.getObject());
	}
}
