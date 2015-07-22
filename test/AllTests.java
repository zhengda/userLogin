import leon.filter.AccessControlFilterTest;
import leon.userlogin.persistence.UserDAOTest;
import leon.util.ExpiringLRUMapTest;
import leon.userlogin.control.ApiServletTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @createTime Jul 22, 2015 7:24:21 AM
 * @project userLogin
 * @author leon
 * @see
 */
@RunWith(Suite.class)
@SuiteClasses({ ApiServletTest.class, ExpiringLRUMapTest.class, UserDAOTest.class, AccessControlFilterTest.class })
public class AllTests {

}
