package name.kan.ppr.model;

import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * @author kan
 * @since 2013-05-14 21:22
 */
public class DatePeriodTest
{
	@Test
	public void testEquals() throws Exception
	{
		assertThat(
				new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15")),
				CoreMatchers.equalTo(
						new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15"))));
		assertThat(
				new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15")),
				CoreMatchers.not(CoreMatchers.equalTo(
						new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2014-05-15")))));
		assertThat(
				new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15")),
				CoreMatchers.not(CoreMatchers.equalTo(
						new DatePeriod(LocalDate.parse("2014-05-14"), LocalDate.parse("2013-05-15")))));
	}

	@Test
	public void testHashCode() throws Exception
	{
		assertEquals(new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15")).hashCode(),
				new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15")).hashCode());
	}

	@Test
	public void testToString() throws Exception
	{
		assertEquals("[2013-05-14,2013-05-15]",
				new DatePeriod(LocalDate.parse("2013-05-14"), LocalDate.parse("2013-05-15")).toString());
	}

	@Test
	public void testGetters() throws Exception
	{
		final LocalDate from = LocalDate.parse("2013-05-14");
		final LocalDate to = LocalDate.parse("2013-05-15");
		final DatePeriod period = new DatePeriod(from, to);
		assertSame(from, period.getFrom());
		assertSame(to, period.getTo());
	}
}
