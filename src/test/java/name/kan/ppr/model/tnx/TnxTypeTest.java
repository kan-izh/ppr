package name.kan.ppr.model.tnx;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kan
 * @since 2013-01-30 21:06
 */
public class TnxTypeTest
{
	@Test
	public void testSetName() throws Exception
	{
		final TnxType tnxType = new TnxType();
		tnxType.setName("a name");
		assertEquals("a name", tnxType.getName());
	}
}
