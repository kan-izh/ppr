package name.kan.ppr.model.txn;

import name.kan.ppr.model.AbstractNamedEntity;

/**
 * @author kan
 * @since 2013-01-25 19:39
 */
public class TxnTypeEntity extends AbstractNamedEntity
{
	private static final long serialVersionUID = -8110368444021167747L;

	private TxnTypeEntity parentType;

	public TxnTypeEntity getParentType()
	{
		return parentType;
	}

	public void setParentType(final TxnTypeEntity parentType)
	{
		this.parentType = parentType;
	}
}
