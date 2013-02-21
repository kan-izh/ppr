package name.kan.ppr.model;

import name.kan.ppr.model.txn.IdentifiedEntity;

/**
 * @author kan
 * @since 2013-01-27 02:36
 */
public abstract class AbstractIdentifiedEntity implements IdentifiedEntity
{
	private Long id;
	private int hashCode;

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public void setId(final long id)
	{
		if(this.id == null)
			this.id = id;
		else if(this.id != id)
			throw new IllegalStateException("Cannot reassign id from " + this.id + " to " + id);
	}

	@Override
	public String toString()
	{
		return getClass().getName() + "#" + id;
	}

	@Override
	public boolean equals(final Object o)
	{
		if(this == o) return true;
		if(!(o instanceof AbstractIdentifiedEntity)) return false;

		final AbstractIdentifiedEntity that = (AbstractIdentifiedEntity) o;

		return this.id != null
				&& this.id.equals(that.id);

	}

	@Override
	public int hashCode()
	{
		if(hashCode == 0)
		{
			hashCode = id != null ? id.hashCode() : super.hashCode();
			if(hashCode == 0)
				hashCode = 42;
		}
		return hashCode;
	}
}
