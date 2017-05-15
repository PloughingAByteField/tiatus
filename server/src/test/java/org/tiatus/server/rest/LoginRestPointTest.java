package org.tiatus.server.rest;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 13/09/2016.
 */
public class LoginRestPointTest {

    @Test
    public void testLogin() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();
        HttpServletRequest httpServletRequest = new MockHttpServletRequest().getMockInstance();
        UserPrincipal p = createUserPrincipalWithRole(null);
        SecurityContext securityContext = createSecurityContext(p, "https");

        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfo, httpServletRequest, securityContext);
        Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Assert.assertEquals(response.getLocation(), new URI("https://127.0.0.1:8080/timing"));
    }

    @Test
    public void testAdminLogin() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();
        HttpServletRequest httpServletRequest = new MockHttpServletRequest().getMockInstance();
        UserPrincipal p = createUserPrincipalWithRole(org.tiatus.role.Role.ADMIN);
        SecurityContext securityContext = createSecurityContext(p, "https");

        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfo, httpServletRequest, securityContext);
        Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Assert.assertEquals(response.getLocation(), new URI("https://127.0.0.1:8080/admin"));
    }

    @Test
    public void testAdjudicatorLogin() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();
        HttpServletRequest httpServletRequest = new MockHttpServletRequest().getMockInstance();
        UserPrincipal p = createUserPrincipalWithRole(org.tiatus.role.Role.ADJUDICATOR);
        SecurityContext securityContext = createSecurityContext(p, "https");

        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfo, httpServletRequest, securityContext);
        Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Assert.assertEquals(response.getLocation(), new URI("https://127.0.0.1:8080/adjudicator"));
    }

    @Test
    public void testNoUserPrincipal() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();
        HttpServletRequest httpServletRequest = new MockHttpServletRequest().getMockInstance();
        SecurityContext securityContext = createSecurityContext(null, "https");

        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfo, httpServletRequest, securityContext);
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testLogoutException() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();

        HttpServletRequest httpServletRequest = new MockHttpServletRequest() {
            @Override
            @Mock
            public String getContextPath() {
                return null;
            }
        }.getMockInstance();
        UserPrincipal p = createUserPrincipalWithRole(null);
        SecurityContext securityContext = createSecurityContext(p, "https");

        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfo, httpServletRequest, securityContext);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    class MockHttpSession extends MockUp<HttpSession> {
        @Mock
        public void invalidate() {}

        @Mock
        public void setAttribute(String name, Object value) {}
    }

    class MockUriInfo extends MockUp<UriInfo> {
        @Mock
        public URI getBaseUri() {
            try {
                return new URI("https://127.0.0.1:8080");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class MockHttpServletRequest extends MockUp<HttpServletRequest> {
        @Mock
        public String getContextPath() {
            return "/";
        }

        @Mock
        public HttpSession getSession() {
            return new MockHttpSession().getMockInstance();
        }
    }


    private TiatusSecurityContext createSecurityContext(UserPrincipal p, String scheme) {
        return new TiatusSecurityContext(p, scheme);
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
