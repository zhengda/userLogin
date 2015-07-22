package leon.userlogin.persistence;

import leon.userlogin.po.User;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @createTime Jul 22, 2015 7:17:46 AM
 * @project userLogin
 * @author leon
 * @see
 */
public class UserDAOTest {
	UserDAO dao;

	@Before
	public void setUp() throws Exception {
		dao = UserDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assert (dao.get("sysadmin") != null);
		assert (StringUtils.equals(dao.get("sysadmin").getPassword(), "asdfzxcv"));

		assert (dao.get("a") == null);
		dao.create(new User("a", "a", "a"));
		assert (dao.get("a") != null);
		assert (StringUtils.equals(dao.get("a").getPassword(), "a"));

		dao.update("a", "b", "b");
		assert (StringUtils.equals(dao.get("a").getPassword(), "b"));

		dao.delete("a");
		assert (dao.get("a") == null);
		assert (dao.get("b") == null);
		assert (dao.get("c") == null);
	}
}
