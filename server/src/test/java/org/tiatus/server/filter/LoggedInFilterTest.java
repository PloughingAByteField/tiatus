package org.tiatus.server.filter;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;
import org.tiatus.server.rest.LoginRestPointTest;
import org.tiatus.service.UserServiceImpl;

import javax.enterprise.inject.Model;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 13/09/2016.
 */
public class LoggedInFilterTest {

    @Test
    public void testDoFilterSkipUrl() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/rest/login";
            }
        }.getMockInstance();
        ServletResponse servletResponse = new MockHttpServletResponse().getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return false;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
        filter.destroy();
    }

    @Test
    public void testAdminUser() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/management/management.html";
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        if (name.equals("principal")) {
                            return createUserPrincipalWithRole(org.tiatus.role.Role.ADMIN);
                        }
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse().getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }

    @Test
    public void testAdjuicatorUser() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/adjudicator/adjudicator.html";
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        if (name.equals("principal")) {
                            return createUserPrincipalWithRole(org.tiatus.role.Role.ADJUDICATOR);
                        }
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse().getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }

    @Test
    public void testTimingUser() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/timing/timing.html";
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        if (name.equals("principal")) {
                            return createUserPrincipalWithRole(org.tiatus.role.Role.TIMING);
                        }
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse().getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }

    @Test
    public void testInvalidUserAccess() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/timing/timing.html";
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        if (name.equals("principal")) {
                            return createUserPrincipalWithRole(org.tiatus.role.Role.ADJUDICATOR);
                        }
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse(){
            @Mock (invocations = 1)
            public void sendRedirect(String location) throws IOException {
                Assert.assertEquals(LoggedInFilter.LOGIN_URL, location);
            }
        }.getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }

    @Test
    public void testNotSetup() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/timing/timing.html";
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse(){
            @Mock (invocations = 1)
            public void sendRedirect(String location) throws IOException {
                Assert.assertEquals(LoggedInFilter.SETUP_URL, location);
            }
        }.getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return false;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }

    @Test
    public void testNotLoggedIn() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return "/timing/timing.html";
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse(){
            @Mock (invocations = 1)
            public void sendRedirect(String location) throws IOException {
                Assert.assertEquals(LoggedInFilter.LOGIN_URL, location);
            }
        }.getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }


    @Test
    public void testAccessSetupAfterSetup() throws Exception {
        LoggedInFilter filter = new LoggedInFilter();
        ServletRequest servletRequest = new MockHttpServletRequest(){
            @Override
            @Mock
            public String getRequestURI() {
                return LoggedInFilter.SETUP_REST_URL;
            }

            @Mock
            public HttpSession getSession() {
                return new MockUp<HttpSession>(){
                    @Mock
                    public Object getAttribute(String name) {
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();

        ServletResponse servletResponse = new MockHttpServletResponse(){
            @Mock (invocations = 1)
            public void sendRedirect(String location) throws IOException {
                Assert.assertEquals(LoggedInFilter.LOGIN_URL, location);
            }
        }.getMockInstance();
        FilterChain chain = new MockFilterChain().getMockInstance();
        FilterConfig config = new MockFilterConfig().getMockInstance();

        UserServiceImpl service = new MockUp<UserServiceImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        }.getMockInstance();

        filter.setUserService(service);
        filter.init(config);
        filter.doFilter(servletRequest, servletResponse, chain);
    }

    class MockHttpServletRequest extends MockUp<HttpServletRequest> {
        @Mock
        public String getRequestURI() {
            return "/";
        }
    }

    class MockHttpServletResponse extends MockUp<HttpServletResponse> {

    }

    class MockFilterChain extends MockUp<FilterChain> {
        @Mock
        public void doFilter ( ServletRequest request, ServletResponse response ) throws IOException, ServletException {

        }
    }

    class MockFilterConfig extends MockUp<FilterConfig> {
        @Mock
        public String getInitParameter(String name) {
            return "/rest/login,/public,/results,/favicon.ico";
        }
    }

    private UserPrincipal createUserPrincipalWithRole(String roleString) {
        UserPrincipal userPrincipal = new UserPrincipal();
        User user = new User();
        user.setUserName("user");
        if (null != roleString) {
            UserRole userRole = new UserRole();
            Role role = new Role();
            role.setRoleName(roleString);
            userRole.setRole(role);
            userRole.setUser(user);
            Set<UserRole> userRoleList = new HashSet<>();
            userRoleList.add(userRole);
            user.setRoles(userRoleList);
        }
        userPrincipal.setUser(user);
        return userPrincipal;
    }
}
