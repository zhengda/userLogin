package leon.userlogin.control;

import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import leon.userlogin.persistence.DAOException;
import leon.userlogin.persistence.UserDAO;
import leon.userlogin.po.User;
import leon.util.ExpiringLRUMap;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @createTime Jul 22, 2015 4:05:45 AM
 * @project userLogin
 * @author leon
 * @see
 */
public class ApiServlet extends HttpServlet {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(ApiServlet.class);

	private static final long serialVersionUID = 1089874389888430014L;
	private UserDAO userDao;

	private int timeUnit = 1 * 60 * 60; //(seconds)
	private int capacityOfTokens = 10 * 1000;
	private ExpiringLRUMap<String, String> tokenMap; //token:username

	@Override
	public void init() throws ServletException {
		userDao = UserDAO.getInstance();
		tokenMap = new ExpiringLRUMap<String, String>(capacityOfTokens, timeUnit);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (checkTokenError(request, response) == null) {
				//deal with get user request
				final String username = request.getParameter("username");
				logger.debug("username=" + username);
				User user = userDao.get(username);
				if (user != null) {
					user.setPassword(null);
				}
				logger.debug("user=" + user);
				sendJSON(response, user);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			//deal with auth login/logout
			if (StringUtils.isNotBlank(request.getHeader("auth"))) {
				switch (AuthType.valueOf(request.getHeader("auth"))) {
					case login: {
						//final String credentials = request.getHeader("Authorization");									
						final String body = extractPostRequestBody(request);
						ObjectMapper mapper = new ObjectMapper();
						Authorization auth = mapper.readValue(body, Authorization.class);
						logger.debug("auth=" + auth);

						String token = UUID.randomUUID().toString(); //generate uuid as token
						tokenMap.put(token, auth.getUsername()); // store token
						sendJSON(response, new Authtoken(token));
						break;
					}
					case logout: {
						final String token = request.getHeader("Authorization");
						System.out.println("token=" + token);
						boolean b = tokenMap.remove(token) != null;
						sendJSON(response, new Message(b ? "success" : "fail"));
						break;
					}
					default:
				}
			} else //

			//deal with  post for User
			if (checkTokenError(request, response) == null) {
				final String body = extractPostRequestBody(request);
				ObjectMapper mapper = new ObjectMapper();
				User user = mapper.readValue(body, User.class);
				logger.debug("user=" + user);

				try {
					boolean b = userDao.create(user);
					sendJSON(response, new Message(b ? "success" : "fail"));
				} catch (DAOException ee) {
					logger.error(ee.toString());
					sendJSON(response, new Error(ee.getMessage()));
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (checkTokenError(request, response) == null) {
				boolean b = userDao.delete(request.getParameter("username"));
				sendJSON(response, new Message(b ? "success" : "fail"));
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (checkTokenError(request, response) == null) {
				final String body = extractPostRequestBody(request);
				ObjectMapper mapper = new ObjectMapper();
				User user = mapper.readValue(body, User.class);
				logger.debug("user=" + user);
				try {
					boolean b = userDao.update(user);
					sendJSON(response, new Message(b ? "success" : "fail"));
				} catch (DAOException ee) {
					logger.error(ee.toString());
					sendJSON(response, new Error(ee.getMessage()));
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void sendTypedResponse(HttpServletRequest request, HttpServletResponse response, Object data) {
		final String acceptedType = request.getHeader("accept");
		if (false) {
		} else if (acceptedType.contains("text/json")) {
			sendJSON(response, data);
		} else if (acceptedType.contains("application/json")) {
			sendJSON(response, data);
		} else if (acceptedType.contains("text/xml")) {
			sendXML(response, data);
		} else if (acceptedType.contains("text/html")) {
			sendHTML(response, data);
		} else {
			sendPlain(response, data);
		}
	}

	private void sendJSON(HttpServletResponse response, Object data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			OutputStream out = response.getOutputStream();
			String body = mapper.writeValueAsString(data);
			logger.info("response.body=" + body);
			out.write(body.getBytes());
			out.flush();
		} catch (Exception e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void sendXML(HttpServletResponse response, Object data) {
		try {
			XMLEncoder enc = new XMLEncoder(response.getOutputStream());
			String body = data.toString();
			logger.info("response.body=" + body);
			enc.writeObject(body);
			enc.close();
		} catch (IOException e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void sendHTML(HttpServletResponse response, Object data) {
		String html_start = "<html><head><title></title></head><body><div>";
		String html_end = "</div></body></html>";
		String body = html_start + data.toString() + html_end;
		sendPlain(response, body);
	}

	private void sendPlain(HttpServletResponse response, Object data) {
		try {
			OutputStream out = response.getOutputStream();
			String body = data.toString();
			logger.info("response.body=" + body);
			out.write(body.getBytes());
			out.flush();
		} catch (IOException e) {
			logger.error(e, e);
			throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	final private static String extractPostRequestBody(HttpServletRequest request) {
		if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
			Scanner s = null;
			try {
				s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return s.hasNext() ? s.next() : "";
		}
		return "";
	}

	private Error checkTokenError(HttpServletRequest request, HttpServletResponse response) {
		Error err = null;
		String header = request.getHeader("Authorization");
		if (header != null && tokenMap.get(header) == null) {
			err = new Error("invalidToken");
			sendJSON(response, err);
		}
		return err;
	}

}

class Error {
	private String error;

	public Error() {
	}

	public Error(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}

class Message {
	private String message;

	public Message() {
	}

	public Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

class Authorization {
	private String username;
	private String password;

	public Authorization() {
	}

	public Authorization(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

class Authtoken {
	private String authtoken;

	public Authtoken() {
	}

	public Authtoken(String authtoken) {
		this.authtoken = authtoken;
	}

	public String getAuthtoken() {
		return authtoken;
	}

	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}
}
