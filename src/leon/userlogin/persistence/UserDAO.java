package leon.userlogin.persistence;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import leon.userlogin.po.User;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:27:31 PM
 * @project userLogin
 * @author leon
 */
public class UserDAO {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(UserDAO.class);

	private static final UserDAO instance = new UserDAO();

	public static final UserDAO getInstance() {
		return instance;
	}

	private UserDAO() {
		create(new User("sysadmin", "asdfzxcv", "To Iterate is Human, to Recurse, Divine."));
	}

	final private int storageCapacity = 100;
	private Map<String, User> userStorage = new Hashtable<String, User>(storageCapacity);

	public boolean create(User user) {
		if (userStorage.size() > storageCapacity) {
			throw new DAOException("exceed the capacity (" + storageCapacity + " users) of storage. ");
		}

		String warningMsg = "";
		if (StringUtils.isBlank(user.getPassword())) {
			warningMsg = "password can't be blank.";
		}
		if (StringUtils.isBlank(user.getUsername())) {
			if (StringUtils.isBlank(warningMsg)) {
				warningMsg = "username can't be blank.";
			} else {
				warningMsg = "username, " + warningMsg;
			}
		}
		if (StringUtils.isNotBlank(warningMsg)) {
			throw new DAOException(warningMsg);
		} else if (userStorage.containsKey(user.getUsername())) {
			throw new DAOException("user exists. ");
		} else {
			userStorage.put(user.getUsername(), user);
			return true;
		}

	}

	public User get(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		try {
			final User userInStorage = userStorage.get(username);
			if (userInStorage != null) {
				User user = new User();
				BeanUtils.copyProperties(user, userInStorage);
				return user;
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

	public boolean update(String username, String password, String description) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return false;
		}
		User user = new User(username, password, description);
		return update(user);
	}

	public boolean update(User user) {
		if (user == null || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return false;
		}
		try {
			final User userInStorage = userStorage.get(user.getUsername());
			if (userInStorage != null) {
				BeanUtils.copyProperties(userInStorage, user);
				return true;
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return false;
	}

	public boolean delete(String username) {
		if (StringUtils.isBlank(username)) {
			return false;
		}
		return userStorage.remove(username) != null;
	}

	public Collection<User> listAll() {
		return userStorage.values();
	}

	public int getCount() {
		return userStorage.size();
	}

	public int getCapacity() {
		return storageCapacity;
	}

}
