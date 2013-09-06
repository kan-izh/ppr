package name.kan.ppr.model.txn;

import java.util.List;

/**
 * @author kan
 * @since 2013-02-16 00:23
 */
public interface TxnRepository
{
	void save(TxnEntity entity);

	List<TxnEntity> findTxns(long first, long count);

	long countTxns();
}
