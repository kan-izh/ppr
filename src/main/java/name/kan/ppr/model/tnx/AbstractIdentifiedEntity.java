package name.kan.ppr.model.tnx;

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
	public boolean isTransient()
	{
		return id == null;
	}
}
