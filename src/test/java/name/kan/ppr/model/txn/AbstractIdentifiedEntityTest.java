package name.kan.ppr.model.txn;

import name.kan.ppr.model.AbstractIdentifiedEntity;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author kan
 * @since 2013-02-13 20:41
 */
public class AbstractIdentifiedEntityTest
{
	@Test(expected = NullPointerException.class)
	public void getIdTransient() throws Exception
	{
		new TestEntity().getId();
	}

	@Test(expected = IllegalStateException.class)
	public void testChangeId() throws Exception
	{
		final TestEntity entity = new TestEntity();
		entity.setId(123);
		assertEquals(123, entity.getId());
		entity.setId(124);
	}

	@Test
	public void testSetId() throws Exception
	{
		final TestEntity entity = new TestEntity();
		entity.setId(123);
		assertEquals(123, entity.getId());
		entity.setId(123);
		assertEquals(123, entity.getId());
	}

	@Test
	public void testToString() throws Exception
	{
		final TestEntity entity = new TestEntity();
		entity.setId(1234567890123456789L);
		assertThat(entity.toString(), Matchers.containsString("1234567890123456789"));
	}

	@Test
	public void testNotEquals() throws Exception
	{
		final TestEntity entity1 = new TestEntity();
		entity1.setId(1);
		final TestEntity entity2 = new TestEntity();
		entity2.setId(2);
		assertThat(entity1, not(equalTo(entity2)));
	}

	@Test
	public void testEquals() throws Exception
	{
		final TestEntity entity1 = new TestEntity();
		entity1.setId(10);
		final TestEntity entity2 = new TestEntity();
		entity2.setId(10);
		assertThat(entity1, equalTo(entity2));
	}

	@Test
	public void testNotEqualsOneNull() throws Exception
	{
		final TestEntity entity1 = new TestEntity();
		entity1.setId(10);
		final TestEntity entity2 = new TestEntity();
		assertThat(entity1, not(equalTo(entity2)));
	}

	@Test
	public void testNotEqualsNulls() throws Exception
	{
		final TestEntity entity1 = new TestEntity();
		final TestEntity entity2 = new TestEntity();
		assertThat(entity1, not(equalTo(entity2)));
	}

	@Test
	public void testHashCodeEquals() throws Exception
	{
		final TestEntity entity1 = new TestEntity();
		entity1.setId(10);
		final TestEntity entity2 = new TestEntity();
		entity2.setId(10);
		assertThat(entity1.hashCode(), equalTo(entity2.hashCode()));
	}

	@Test
	public void testHashCodeEqualsZero() throws Exception
	{
		final TestEntity entity1 = new TestEntity();
		entity1.setId(0);
		final TestEntity entity2 = new TestEntity();
		entity2.setId(0);
		assertThat(entity1.hashCode(), equalTo(entity2.hashCode()));
	}

	@Test
	public void invariantHashCodeForTransients() throws Exception
	{
		final TestEntity testEntity = new TestEntity();
		final int hashCodeBefore = testEntity.hashCode();
		testEntity.setId(12);
		assertEquals(hashCodeBefore,  testEntity.hashCode());
	}

	private static class TestEntity extends AbstractIdentifiedEntity
	{}
}
