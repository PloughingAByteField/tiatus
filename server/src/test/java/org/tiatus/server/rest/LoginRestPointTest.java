package org.tiatus.server.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 13/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class LoginRestPointTest {

    @Mock
    private UriInfo uriInfoMock;

    @Mock
    private URI uriMock;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private HttpSession httpSessionMock;

    @Test
    public void testLogin() throws Exception {
        UserPrincipal p = createUserPrincipalWithRole(null);
        SecurityContext securityContext = createSecurityContext(p, "https");

        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(httpServletRequestMock.getContextPath()).thenReturn("https://127.0.0.1:8080/");
        when(uriInfoMock.getBaseUri()).thenReturn(uriMock);
        
        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfoMock, httpServletRequestMock, securityContext);
        Assertions.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Assertions.assertEquals(new URI("https://127.0.0.1:8080/timing"), response.getLocation());
    }

    @Test
    public void testAdminLogin() throws Exception {
        UserPrincipal p = createUserPrincipalWithRole(org.tiatus.role.Role.ADMIN);
        SecurityContext securityContext = createSecurityContext(p, "https");

        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(httpServletRequestMock.getContextPath()).thenReturn("https://127.0.0.1:8080/");
        when(uriInfoMock.getBaseUri()).thenReturn(uriMock);
        
        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfoMock, httpServletRequestMock, securityContext);
        Assertions.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Assertions.assertEquals(new URI("https://127.0.0.1:8080/admin"), response.getLocation());
    }

    @Test
    public void testAdjudicatorLogin() throws Exception {
        UserPrincipal p = createUserPrincipalWithRole(org.tiatus.role.Role.ADJUDICATOR);
        SecurityContext securityContext = createSecurityContext(p, "https");

        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(httpServletRequestMock.getContextPath()).thenReturn("https://127.0.0.1:8080/");
        when(uriInfoMock.getBaseUri()).thenReturn(uriMock);
        
        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfoMock, httpServletRequestMock, securityContext);
        Assertions.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Assertions.assertEquals(new URI("https://127.0.0.1:8080/adjudicator"), response.getLocation());
    }

    @Test
    public void testNoUserPrincipal() throws Exception {
        SecurityContext securityContext = createSecurityContext(null, "https");

        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(httpServletRequestMock.getContextPath()).thenReturn("https://127.0.0.1:8080/");
        when(uriInfoMock.getBaseUri()).thenReturn(uriMock);
        
        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfoMock, httpServletRequestMock, securityContext);
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testLogoutException() throws Exception {
        UserPrincipal p = createUserPrincipalWithRole(null);
        SecurityContext securityContext = createSecurityContext(p, "https");

        when(httpServletRequestMock.getContextPath()).thenReturn(null);
        
        LoginRestPoint logoutRestPoint = new LoginRestPoint();

        Response response = logoutRestPoint.login(uriInfoMock, httpServletRequestMock, securityContext);
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
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
