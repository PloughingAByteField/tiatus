package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by johnreynolds on 27/08/2016.
 */
@WebFilter(
        filterName="LoginServlet",
        urlPatterns={"/*"},
        initParams = {
            @WebInitParam(name = "pass-through", value = "/rest/login,/public,/results")
        }
)
public class LoggedInFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(LoggedInFilter.class);

    private List<String> skipUrls = new ArrayList<>();

    public static final String LOGIN_URL = "/login.html";

    @Override
    public void init(FilterConfig config) throws ServletException {
        skipUrls.add(LOGIN_URL);
        String urls = config.getInitParameter("pass-through");
        StringTokenizer token = new StringTokenizer(urls, ",");
        while (token.hasMoreTokens()) {
            skipUrls.add(token.nextToken());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        LOG.debug("in doFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestedUrl = request.getRequestURI();
        if (checkUrl(requestedUrl)){
            LOG.debug("skipping " + requestedUrl);
        } else {
            HttpSession session = request.getSession();

            if (session != null && session.getAttribute("principal") != null) {
                LOG.debug("logged in");
            } else {
                LOG.debug("not logged in");
                response.sendRedirect(LOGIN_URL);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean checkUrl(String url) {
        if (skipUrls.contains(url)){
            return true;
        }

        StringTokenizer token = new StringTokenizer(url, "/");
        if (token.hasMoreTokens()) {
            String root = "/" + token.nextToken();
            if (skipUrls.contains(root)) {
                return true;
            }
        }

        return false;
    }
}
