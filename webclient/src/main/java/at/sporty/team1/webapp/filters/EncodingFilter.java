package at.sporty.team1.webapp.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sereGkaluv on 02.01.16.
 */
@WebFilter({"*.jsf", "*.xhtml", "*.faces", "/faces/"})
public class EncodingFilter implements HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig)
	throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
