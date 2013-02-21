package name.kan.ppr.model;

import com.google.common.base.Objects;
import org.joda.time.LocalDate;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author kan
 * @since 2013-02-15 20:01
 */
public class DatePeriod implements Serializable
{
	private static final long serialVersionUID = 3799120371926946804L;

	private final LocalDate from;
	private final LocalDate to;

	public DatePeriod(final LocalDate from, final LocalDate to)
	{
		this.from = checkNotNull(from, "from");
		this.to = checkNotNull(to, "to");
	}

	public LocalDate getFrom()
	{
		return from;
	}

	public LocalDate getTo()
	{
		return to;
	}

	@Override
	public boolean equals(final Object o)
	{
		if(this == o) return true;
		if(!(o instanceof DatePeriod)) return false;

		final DatePeriod that = (DatePeriod) o;

		return from.equals(that.from)
				&& to.equals(that.to);

	}

	@Override
	public int hashCode()
	{
		int result = from.hashCode();
		result = 31 * result + to.hashCode();
		return result;
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this)
				.add("from", from)
				.add("to", to)
				.toString();
	}
}
