package leon.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import leon.userlogin.persistence.UserDAO;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:26:39 PM
 * @project userLogin
 * @author leon
 */
public class SessionCounterListener implements HttpSessionListener, ServletContextListener, ServletContextAttributeListener {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(SessionCounterListener.class);

	private static List<String> sessions = new ArrayList<String>();

	public static List<String> getSessions() {
		return sessions;
	}

	private int count;

	private ServletContext context = null;

	public void setContext(HttpSessionEvent e) {
		e.getSession().getServletContext().setAttribute("onLine", new Integer(count));
	}

	@Override
	public void sessionCreated(HttpSessionEvent e) {
		count++;
		setContext(e);
		HttpSession session = e.getSession();
		sessions.add(session.getId());
		session.setAttribute("counter", this);

		final UserDAO userDao = UserDAO.getInstance();
		session.setAttribute("userDao", userDao);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		count--;
		setContext(e);
		HttpSession session = e.getSession();
		sessions.remove(session.getId());
		session.setAttribute("counter", this);

	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		logger.info(e.toString());
		context = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		logger.info(e.toString());
		context = e.getServletContext();
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent e) {
		logger.info("attributeAdded " + e.getName() + "=" + e.getValue());
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent e) {
		logger.info("attributeRemoved " + e.getName() + "=" + e.getValue());
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent e) {
		logger.info("attributeReplaced " + e.getName() + "=" + e.getValue());
	}
}
