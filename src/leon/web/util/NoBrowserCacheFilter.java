package leon.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/*****************************************
 
        <filter>
                <filter-name>NoBrowserCacheFilter</filter-name>
                <filter-class>leon.util.filter.NoBrowserCacheFilter</filter-class>
        </filter>
 
        <filter-mapping>
                <filter-name>NoBrowserCacheFilter</filter-name>
                <url-pattern>*.jsp</url-pattern>
        </filter-mapping>
        <filter-mapping>
                <filter-name>NoBrowserCacheFilter</filter-name>
                <url-pattern>*.do</url-pattern>
        </filter-mapping>
        
 * 
 *
 * @createTime Jul 21, 2015 10:26:45 PM
 * @project userLogin
 * @author leon
 */
public class NoBrowserCacheFilter implements Filter {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(NoBrowserCacheFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		HttpServletResponse hresponse = (HttpServletResponse) response;
		hresponse.setDateHeader("Expires", 0L); // Date in the past
		hresponse.setHeader("Pragma", "No-cache"); //HTTP 1.0
		hresponse.setHeader("Cache-Control", "no-cache, " + "no-store,must-revalidate, max-age=0, " + "post-check=0, pre-check=0"); //HTTP 1.1
		hresponse.setHeader("X-UA-Compatible", "IE=8");
		try {
			chain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
	}

}