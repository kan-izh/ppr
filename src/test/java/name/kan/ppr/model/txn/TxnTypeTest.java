package name.kan.ppr.model.txn;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kan
 * @since 2013-01-30 21:06
 */
public class TxnTypeTest
{
	@Test
	public void testSetName() throws Exception
	{
		final TxnType txnType = new TxnType();
		txnType.setName("a name");
		assertEquals("a name", txnType.getName());
	}
}
