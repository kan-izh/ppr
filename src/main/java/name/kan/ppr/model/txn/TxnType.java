package name.kan.ppr.model.txn;

import javax.annotation.Nonnull;

/**
 * @author kan
 * @since 2013-01-25 19:39
 */
public class TxnType extends AbstractIdentifiedEntity
{
	private @Nonnull String name;

	public @Nonnull String getName()
	{
		return name;
	}

	public void setName(final @Nonnull String name)
	{
		this.name = name;
	}
}
