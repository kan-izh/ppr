package name.kan.ppr.model.txn;

/**
 * @author kan
 * @since 2013-01-27 02:36
 */
public abstract class AbstractIdentifiedEntity implements IdentifiedEntity
{
	private Long id;

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public void setId(final long id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return getClass().getName() + "#" + id;
	}
}
