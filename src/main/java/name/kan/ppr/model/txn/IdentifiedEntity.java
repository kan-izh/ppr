package name.kan.ppr.model.txn;

import java.io.Serializable;

/**
 * @author kan
 * @since 2013-01-27 02:34
 */
public interface IdentifiedEntity extends Serializable
{
	long getId();
	void setId(long id);
}
