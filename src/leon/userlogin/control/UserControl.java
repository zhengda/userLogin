package leon.userlogin.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import leon.userlogin.persistence.DAOException;
import leon.userlogin.persistence.UserDAO;
import leon.userlogin.po.User;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 *
 * @createTime Jul 22, 2015 9:02:35 AM
 * @project userLogin
 * @author leon
 */
public class UserControl extends HttpServlet {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(UserControl.class);
	private static final long serialVersionUID = -6481433404587587031L;

	private UserDAO userDao;

	@Override
	public void init() throws ServletException {
		userDao = UserDAO.getInstance();
	}

	@Override
	public void destroy() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		final String description = request.getParameter("description");

		String action = request.getParameter("action");
		UserAction userAction = UserAction.valueOf(action);
		switch (userAction) {
			case list:
				java.util.Collection<User> users = userDao.listAll();
				//logger.debug("users="+users);
				request.setAttribute("users", users);
				forward(request, response, "userList");
				return;
				//break;
			case create: {
				forward(request, response, "userCreate");
				return;
			}
			case save: {
				User user = new User(username, password, description);
				try {
					boolean b = userDao.create(user);
					if (!b) {
						logger.warn("fail to create user " + username);
						request.setAttribute("warningMsg", "fail to create user " + username);
					}
				} catch (DAOException e) {
					logger.error(e, e);
					request.setAttribute("errorMsg", e.toString());
				} catch (NullPointerException e) {
					logger.error(e, e);
				}
				if (request.getAttribute("errorMsg") != null || request.getAttribute("warningMsg") != null) {
					if (user != null) {
						request.setAttribute("target", user);
					}
					forward(request, response, "userCreate");
					return;
				}
				forward(request, response, "userControl?action=list");
				return;
			}
			case show:
			case edit: {
				User user = userDao.get(username);
				if (user != null) {
					request.setAttribute("target", user);
				}
				forward(request, response, "userEdit");
				return;
			}
			case update: {
				if (org.apache.commons.lang.StringUtils.equals(username, "sysadmin")) {
					request.setAttribute("warningMsg", "You are not allowed to modify " + username);
					forward(request, response, "userControl?action=list");
					return;
				}

				User user = new User(username, password, description);
				boolean b = userDao.update(user);
				if (!b) {
					logger.warn("fail to update user " + username);
					request.setAttribute("errorMsg", "fail to update user " + username);

					String warningMsg = "";
					if (StringUtils.isBlank(password)) {
						warningMsg = "password can't be blank.";
					}
					if (StringUtils.isBlank(username)) {
						if (StringUtils.isBlank(warningMsg)) {
							warningMsg = "username can't be blank.";
						} else {
							warningMsg = "username, " + warningMsg;
						}
					}
					if (StringUtils.isNotBlank(warningMsg)) {
						request.setAttribute("warningMsg", warningMsg);
					}

				}
				if (request.getAttribute("errorMsg") != null || request.getAttribute("warningMsg") != null) {
					if (user != null) {
						request.setAttribute("target", user);
					}
					forward(request, response, "userEdit");
					return;
				}
				forward(request, response, "userControl?action=list");
				return;
			}
			case delete: {
				if (org.apache.commons.lang.StringUtils.equals(username, "sysadmin")) {
					request.setAttribute("warningMsg", "You are not allowed to delete " + username);
					forward(request, response, "userControl?action=list");
					return;
				}

				boolean b = userDao.delete(username);
				if (!b) {
					logger.warn("fail to delete user " + username);
					request.setAttribute("warningMsg", "fail to delete user " + username);
				}
				forward(request, response, "userControl?action=list");
				return;
			}
			default:
		}

		response.sendRedirect("index");
	}

	private void forward(HttpServletRequest aRequest, HttpServletResponse aResponse, String forwardingPage) throws ServletException, IOException {
		RequestDispatcher dispatcher = aRequest.getRequestDispatcher(forwardingPage);
		dispatcher.forward(aRequest, aResponse);
	}
}
