package leon.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:26:55 PM
 * @project userLogin
 * @author leon
 */
public class EncodingFilter implements Filter {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(EncodingFilter.class);

	private String encoding = "UTF-8";

	@Override
	public void init(FilterConfig config) throws ServletException {
		String enc = config.getInitParameter("encoding");
		logger.info("Filter Initialization at encoding=" + enc);
		if (enc != null && !enc.equals("")) {
			this.encoding = enc;
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(this.encoding);
		chain.doFilter(request, response);
	}

}
