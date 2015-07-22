package leon.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import leon.util.ExpiringLRUMap;
import leon.web.util.ToStringUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:27:46 PM
 * @project userLogin
 * @author leon
 */
public class AccessControlFilter implements Filter {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(AccessControlFilter.class);

	private int timeUnit = 1 * 60 * 60; //(seconds)
	private int maxAccessCountPerTimeUnit = Integer.MAX_VALUE; //(seconds)

	private int sizeOfIpTable = 10 * 1000;
	private long timeoutToExpireBanIp = 1 * 60 * 60; //(seconds) 1 hours 

	private ExpiringLRUMap<String, Long> incomingIpMap;
	private ExpiringLRUMap<String, Long> banIpMap;

	private String[] allowedURIs;

	@Override
	public void destroy() {
		logger.info("banIpMap=" + leon.web.util.ToStringUtils.toString(banIpMap));
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		{
			final String k = "timeUnit";
			if (StringUtils.isNotBlank(config.getInitParameter(k))) {
				try {
					timeUnit = Integer.parseInt(config.getInitParameter(k));
					logger.info("timeUnit=" + timeUnit);
				} catch (Exception e) {
					logger.error(e, e);
				}
			}
		}

		{
			final String k = "maxAccessCountPerTimeUnit";
			if (StringUtils.isNotBlank(config.getInitParameter(k))) {
				try {
					maxAccessCountPerTimeUnit = Integer.parseInt(config.getInitParameter(k));
					logger.info("maxAccessCountPerTimeUnit=" + maxAccessCountPerTimeUnit);
				} catch (Exception e) {
					logger.error(e, e);
				}
			}
		}

		{
			final String k = "sizeOfIpTable";
			if (StringUtils.isNotBlank(config.getInitParameter(k))) {
				try {
					sizeOfIpTable = Integer.parseInt(config.getInitParameter(k));
					logger.info("sizeOfIpTable=" + sizeOfIpTable);
				} catch (Exception e) {
					logger.error(e, e);
				}
			}
		}

		incomingIpMap = new ExpiringLRUMap<String, Long>(sizeOfIpTable, timeUnit);
		banIpMap = new ExpiringLRUMap<String, Long>(sizeOfIpTable, timeoutToExpireBanIp);

		{
			final String k = "allowedURI";
			if (StringUtils.isNotBlank(config.getInitParameter(k))) {
				String p = config.getInitParameter(k);
				List<String> uris = new java.util.ArrayList<String>();
				for (String s : StringUtils.split(p, ",")) {
					s = config.getServletContext().getContextPath() + s;
					s = StringUtils.startsWith(s, "/") ? s : "/" + s;
					logger.info("allowedURI=" + s);
					uris.add(s);
				}
				allowedURIs = uris.toArray(new String[] {});
				logger.info("allowedURIs=" + ToStringUtils.toString(allowedURIs));
			}
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		final String ip = request.getRemoteAddr();
		{ //control of access count per IP 
			if (banIpMap.containsKey(ip)) {
				response.getWriter().print(
						"<html><head><title>Error</title></head><body><h1>403 forbidden<br/>You are banned for reaching access limit.</h1></body></html>");
				return;
			}

			Long accessCount = incomingIpMap.get(ip);
			if (accessCount == null) {
				accessCount = 0L;
			}
			accessCount++;
			incomingIpMap.put(ip, accessCount);

			//maxAccessCountPerTimeUnit = 100;//for test only
			if (accessCount > maxAccessCountPerTimeUnit) {
				long timeoutToExpireBanIp = this.timeoutToExpireBanIp + RandomUtils.nextLong() % this.timeoutToExpireBanIp; //t再加上隨機0~t的時間長度
				logger.warn("ip " + ip + " access count=" + accessCount + " ;reachs maxAccessCountPerTimeUnit " + maxAccessCountPerTimeUnit
						+ " -> go block it in " + timeoutToExpireBanIp + "s ");
				banIpMap.put(ip, timeoutToExpireBanIp);
			}
		}

		{ //control of access of entris
			final boolean isAllowedEntries = startsWithAnyIgnoreCase(request.getRequestURI(), allowedURIs);
			if (!isAllowedEntries && request.getSession().getAttribute("user") == null) {
				logger.info("ip " + ip + " is tring to access " + request.getRequestURI());
				//response.getWriter().print("<h1>403 forbidden<br/>Not allowed to access " + request.getRequestURI());
				response.getWriter().print(
						"<html><head><title>Error</title></head><body><h1>403 Forbidden</h1> try <a href=\"login.jsp\" autofocus>login</a>!</body></html>");
				return;
			}
		}

		chain.doFilter(req, res);
	}

	final public static boolean startsWithAnyIgnoreCase(String t, String[] ss) {
		for (String s : ss) {
			if (startsWithAnyIgnoreCase(t, s)) {
				return true;
			}
		}
		return false;
	}

	final public static boolean startsWithAnyIgnoreCase(String t, String s) {
		return StringUtils.startsWithIgnoreCase(t, s);
	}
}