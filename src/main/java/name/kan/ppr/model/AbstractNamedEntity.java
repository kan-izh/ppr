package name.kan.ppr.model;

import javax.annotation.Nonnull;

/**
 * @author kan
 * @since 2013-02-15 20:31
 */
public class AbstractNamedEntity extends AbstractIdentifiedEntity
{
	private @Nonnull String name;

	public @Nonnull
	String getName()
	{
		return name;
	}

	public void setName(final @Nonnull String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return super.toString()+
				"{name='" + name + '\'' +
				'}';
	}
}
