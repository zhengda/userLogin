package leon.userlogin.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import leon.userlogin.po.User;
import leon.web.util.ToStringUtils;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @createTime Jul 22, 2015 7:16:48 AM
 * @project userLogin
 * @author leon
 * @see
 */
public class ApiServletTest {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(ApiServletTest.class);

	//String apiEntry = "http://localhost:8080/userLogin/api/user";
	String apiEntry = "http://ptter.noip.me:7899/userLogin/api/user";
	String authtoken;

	@Before
	public void setUp() throws Exception {
		// do POST with header "auth=login" to obtain an authtoken
		URL url = new URL(apiEntry);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("auth", "login");
		conn.setRequestProperty("Content-Type", "application/json");

		String body = "{\"username\":\"sysadmin\",\"password\":\"asdfzxcv\"}";
		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes());
		os.flush();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		StringWriter output = new StringWriter();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		body = output.toString();
		logger.info("response.body=" + body);
		conn.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		try {
			Authtoken token = mapper.readValue(body, Authtoken.class);
			logger.debug("auth=" + ToStringUtils.toString(token));
			authtoken = token.getAuthtoken();
			assert (StringUtils.isNotBlank(authtoken));
		} catch (Exception e) {
			logger.error(e.toString());
			Error err = mapper.readValue(body, Error.class);
			logger.debug("error=" + ToStringUtils.toString(err));
		}
	}

	@After
	public void tearDown() throws Exception {
		// do POST with header "auth=logout" to invalidate an authtoken
		URL url = new URL(apiEntry);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("auth", "logout");
		conn.setRequestProperty("Authorization", authtoken);
		conn.setRequestProperty("Content-Type", "application/json");

		String body = "{\"username\":\"sysadmin\",\"password\":\"asdfzxcv\"}";
		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes());
		os.flush();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringWriter output = new StringWriter();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		body = output.toString();
		logger.info("response.body=" + body);
		conn.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		try {
			Message msg = mapper.readValue(body, Message.class);
			assert (StringUtils.equals(msg.getMessage(), "success"));
			logger.debug("msg=" + ToStringUtils.toString(msg));
		} catch (Exception e) {
			logger.error(e.toString());
			Error err = mapper.readValue(body, Error.class);
			logger.debug("error=" + ToStringUtils.toString(err));
		}
	}

	@Test
	public void test() throws IOException {
		testPOST();
		testGET();
		testPUT();
		testDELETE();
	}

	@Test
	public void testPOSTs() throws IOException {
		for (int i = 0; i < 95; i++) {
			final String username = "sysadminx" + i;
			testDELETE(username);
			testPOST(username);
			assert (i <= 100);//current capacity <=100
		}
	}

	public void testPOSTsLongTerm() throws IOException {
		for (int i = 0; i < 100000; i++) {
			final String username = "sysadminx" + i;
			testDELETE(username);
			testPOST(username);
			System.out.println("i=" + i);

			/*
			 * will be ban when i=499 by AccessControlFilter
			 */
		}
	}

	public void testPOST() throws IOException {
		testPOST("sysadminx");
	}

	public void testPOST(String username) throws IOException {
		URL url = new URL(apiEntry);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", authtoken);
		conn.setRequestProperty("Content-Type", "application/json");

		String body = "{\"username\":\"" + username + "\",\"password\":\"asdfzxcv\",\"description\":\"descx\"}";
		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes());
		os.flush();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringWriter output = new StringWriter();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		body = output.toString();
		logger.info("response.body=" + body);
		conn.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		try {
			Message msg = mapper.readValue(body, Message.class);
			assert (StringUtils.equals(msg.getMessage(), "success"));
			logger.debug("msg=" + ToStringUtils.toString(msg));
		} catch (Exception e) {
			logger.error(e.toString());
			Error err = mapper.readValue(body, Error.class);
			logger.debug("error=" + ToStringUtils.toString(err));
		}
	}

	public void testGET() throws IOException {
		String username = "sysadminx";
		testGET(username);
	}

	public void testGET(String username) throws IOException {
		URL url = new URL(apiEntry + "?username=" + username);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", authtoken);
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringWriter output = new StringWriter();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		String body = output.toString();
		logger.info("response.body=" + body);
		conn.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		try {
			User user = mapper.readValue(body, User.class);
			assert (user != null);
			assert (StringUtils.equals(user.getUsername(), username));
			logger.debug("user=" + ToStringUtils.toString(user));
		} catch (Exception e) {
			logger.error(e.toString());
			Error err = mapper.readValue(body, Error.class);
			logger.debug("error=" + ToStringUtils.toString(err));
		}
	}

	public void testPUT() throws IOException {
		URL url = new URL(apiEntry);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("Authorization", authtoken);
		conn.setRequestProperty("Content-Type", "application/json");

		String body = "{\"username\":\"sysadminx\",\"password\":\"xxxxxxxxxxx\",\"description\":\"xxxxxxxxxxxxxx\"}";
		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes());
		os.flush();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringWriter output = new StringWriter();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		body = output.toString();
		logger.info("response.body=" + body);
		conn.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		try {
			Message msg = mapper.readValue(body, Message.class);
			assert (StringUtils.equals(msg.getMessage(), "success"));
			logger.debug("msg=" + ToStringUtils.toString(msg));
		} catch (Exception e) {
			logger.error(e.toString());
			Error err = mapper.readValue(body, Error.class);
			logger.debug("error=" + ToStringUtils.toString(err));
		}
	}

	public void testDELETE() throws IOException {
		String username = "sysadminx";
		testDELETE(username);

	}

	public void testDELETE(String username) throws IOException {
		URL url = new URL(apiEntry + "?username=" + username);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("Authorization", authtoken);
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringWriter output = new StringWriter();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		String body = output.toString();
		logger.info("response.body=" + body);
		conn.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		try {
			Message msg = mapper.readValue(body, Message.class);
			assert (StringUtils.equals(msg.getMessage(), "success"));
			logger.debug("msg=" + ToStringUtils.toString(msg));
		} catch (Exception e) {
			logger.error(e.toString());
			Error err = mapper.readValue(body, Error.class);
			logger.debug("error=" + ToStringUtils.toString(err));
		}
	}
}
