package leon.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @createTime Jul 22, 2015 7:26:23 AM
 * @project userLogin
 * @author leon
 * @see
 */
public class AccessControlFilterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", "asdfasdf"));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", " asdfasdf") == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", "1234") == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", "a"));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", "as"));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", "") == false);

		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "" }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "", " " }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "", null }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "", " a" }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { null, "" }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { " ", "" }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { " ", "" }) == false);
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "a ", "a" }));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { " ", "asd" }));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { " ", "a" }));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "asdf", null }));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "asdf", "" }));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { "", "asdf" }));
		assert (AccessControlFilter.startsWithAnyIgnoreCase("asdf", new String[] { null, "asdf" }));
	}

}
