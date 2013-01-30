package name.kan.ppr.model.tnx;

import javax.annotation.Nonnull;

/**
 * @author kan
 * @since 2013-01-25 19:39
 */
public class TnxType extends AbstractIdentifiedEntity
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
