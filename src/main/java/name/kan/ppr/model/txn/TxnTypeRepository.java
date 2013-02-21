package name.kan.ppr.model.txn;

/**
 * @author kan
 * @since 2013-01-30 20:54
 */
public interface TxnTypeRepository
{
	TxnTypeEntity obtainByName(String name);
}
