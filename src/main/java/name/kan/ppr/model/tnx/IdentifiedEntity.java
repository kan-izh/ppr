package name.kan.ppr.model.tnx;

/**
 * @author kan
 * @since 2013-01-27 02:34
 */
public interface IdentifiedEntity
{
	boolean isTransient();
	long getId();
	void setId(long id);
}
