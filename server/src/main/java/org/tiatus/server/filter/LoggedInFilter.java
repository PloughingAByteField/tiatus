package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.role.Role;
import org.tiatus.service.UserService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
            @WebInitParam(name = "pass-through", value = "/rest,/assets,/public,/favicon.ico,/results,/polyfills,/vendor,/common")
        }
)
public class LoggedInFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(LoggedInFilter.class);

    private List<String> skipUrls = new ArrayList<>();

    public static final String LOGIN_URL = "/login.html";
    public static final String SETUP_URL = "/setup/setup.html";
    public static final String SETUP_REST_URL = "/rest/setup/user";

    private UserService userService = null;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        skipUrls.add(LOGIN_URL);
        skipUrls.add(SETUP_URL);
        skipUrls.add(SETUP_REST_URL);
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
                // are we requesting something we are allowed to request
                UserPrincipal p = (UserPrincipal)session.getAttribute("principal");
                StringTokenizer token = new StringTokenizer(requestedUrl, "/");
                String root = "";
                if (token.hasMoreTokens()) {
                    root = token.nextToken();
                }

                boolean logoutUser = ! isValidAccess(root, p);
                if (logoutUser) {
                    LOG.warn("Logging out user for invalid page access to " + requestedUrl);
                    session.invalidate();
                    response.sendRedirect(LOGIN_URL);
                    return;
                }
            } else {
                if (isSetup()) {
                    LOG.debug("not logged in when fetching " + requestedUrl);
                    response.sendRedirect(LOGIN_URL);
                } else {
                    LOG.debug("not setup when fetching " + requestedUrl);
                    response.sendRedirect(SETUP_URL);
                }
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isValidAccess(String root, UserPrincipal p) {
        if ("rest".equals(root) || "common".equals(root) || "results".equals(root) || "test".equals(root) || "polyfills".equals(root) || "vendor".equals(root)) {
            return true;
        }

        String userRole = getUserRole(p);
        return checkAccess(userRole, root);
    }

    private boolean checkAccess(String userRole, String root) {
        if (userRole.equals(Role.ADMIN) && "management".equals(root)) {
            return true;
        }

        if (userRole.equals(Role.ADJUDICATOR) && "adjudicator".equals(root)) {
            return true;
        }

        if (userRole.equals(Role.TIMING) && "timing".equals(root)) {
            return true;
        }

        return false;
    }

    private String getUserRole(UserPrincipal p) {
        if (TiatusSecurityContext.isUserInRole(p, Role.ADMIN)) {
            return Role.ADMIN;

        } else if (TiatusSecurityContext.isUserInRole(p, Role.ADJUDICATOR)) {
            return Role.ADJUDICATOR;

        } else if (TiatusSecurityContext.isUserInRole(p, Role.TIMING)) {
            return Role.TIMING;
        }
        return "";
    }

    @Override
    public void destroy() {
        // nothing to destroy
    }

    private boolean isSetup() {
        return userService.hasAdminUser();
    }

    private boolean checkUrl(String url) {
        if (url.matches("\\/login/*.*")) {
            return true;
        }

        if (isSetup() && url.equals(SETUP_REST_URL)) {
            LOG.warn("tried to access setup rest url after setup");
            return false;
        }

        if (skipUrls.contains(url)) {
            return true;
        }

        return checkBasePath(url);
    }

    private boolean checkBasePath(String url) {
        StringTokenizer token = new StringTokenizer(url, "/");
        if (token.hasMoreTokens()) {
            String root = "/" + token.nextToken();
            if (skipUrls.contains(root)) {
                return true;
            }

            if ("/setup".equals(root) && !isSetup()) {
                return true;
            }
        }

        return false;
    }
}
