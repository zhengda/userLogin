package leon.userlogin.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import leon.userlogin.persistence.UserDAO;

/**
 * 
 *
 * @createTime Jul 22, 2015 9:02:21 AM
 * @project userLogin
 * @author leon
 */
public class LoginControl extends HttpServlet {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(LoginControl.class);
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
		final leon.userlogin.po.User user = userDao.get(username);

		if (user != null && password != null && password.equals(user.getPassword())) {
			request.getSession().setAttribute("user", user);
			response.sendRedirect("index");
			return;
		}

		forward(request, response, "login");
	}

	private void forward(HttpServletRequest aRequest, HttpServletResponse aResponse, String forwardingPage) throws ServletException, IOException {
		RequestDispatcher dispatcher = aRequest.getRequestDispatcher(forwardingPage);
		dispatcher.forward(aRequest, aResponse);
	}
}
