package leon.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:27:39 PM
 * @project userLogin
 * @author leon
 */
public class ExpiringLRUMapTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExpiringLRU() throws InterruptedException {
		int capacity = 2;
		int ttl = 2;//seconds

		ExpiringLRUMap<Integer, Integer> m = new ExpiringLRUMap<Integer, Integer>(capacity, ttl);

		m.put(1, 1);
		m.put(2, 2);
		m.put(3, 3); // capacity overflow and remove element 1
		assert (m.get(1) == null);

		System.out.println(m.get(1));
		System.out.println(m.get(2));
		System.out.println(m.get(3));

		assert (m.get(1) == null);
		assert (m.get(2) != null);
		assert (m.get(3) != null);

		Thread.sleep(3 * 1000); // now all expired

		System.out.println(m.get(1));
		System.out.println(m.get(2));
		System.out.println(m.get(3));

		assert (m.get(1) == null);
		assert (m.get(2) == null);
		assert (m.get(3) == null);

	}
}
