package at.sporty.team1.webapp.filters;

import at.sporty.team1.util.SessionConstants;

import javax.faces.bean.SessionScoped;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sereGkaluv on 02.01.16.
 */
@WebFilter({"*.jsf", "*.xhtml", "*.faces", "/faces/"})
public class SportyFilter implements HttpFilter {
	private static final String TOURNAMENT_OVERVIEW_PAGE = "/tournament_overview";

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");

		String path = request.getServletPath();
		if (path.startsWith(TOURNAMENT_OVERVIEW_PAGE)) {
			if (request.getSession().getAttribute(SessionConstants.ACTIVE_TOURNAMENT.getConstant()) == null) {
				response.sendRedirect("");
			}
		}

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
