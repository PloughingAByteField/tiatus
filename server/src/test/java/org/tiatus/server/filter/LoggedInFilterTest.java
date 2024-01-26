package org.tiatus.server.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.tiatus.auth.UserPrincipal;
// import org.tiatus.entity.Role;
// import org.tiatus.entity.User;
// import org.tiatus.entity.UserRole;
// import org.tiatus.service.UserServiceImpl;

// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 13/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class LoggedInFilterTest {

    private LoggedInFilter filter;

    // @Mock
    // private HttpServletRequest httpServletRequestMock;

    // @Mock
    // private HttpServletResponse httpServletResponseMock;

    // @Mock
    // private FilterChain filterChainMock;

    // @Mock
    // private FilterConfig filterConfigMock;

    // @Mock
    // private UserServiceImpl userServiceMock;

    // @Mock
    // private HttpSession httpSessionMock;

    // @BeforeEach
    // public void setup() throws Exception {
    //     filter = new LoggedInFilter();
    // }    

    // @Test
    // public void testNotSetup() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/rest/login");
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(1)).sendRedirect("/login");
    // }

    // @Test
    // public void testDoSetup() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/rest/login");
    //     when(userServiceMock.hasAdminUser()).thenReturn(false);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(1)).sendRedirect("/setup");
    // }

    // @Test
    // public void testAdminUser() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/admin/management.html");
    //     when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
    //     when(httpSessionMock.getAttribute("principal")).thenReturn(createUserPrincipalWithRole(org.tiatus.role.Role.ADMIN));
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(0)).sendRedirect(anyString());
    // }

    // @Test
    // public void testAdjuicatorUser() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/adjudicator/adjudicator.html");
    //     when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
    //     when(httpSessionMock.getAttribute("principal")).thenReturn(createUserPrincipalWithRole(org.tiatus.role.Role.ADJUDICATOR));
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(0)).sendRedirect(anyString());
    // }

    // @Test
    // public void testTimingUser() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/timing/timing.html");
    //     when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
    //     when(httpSessionMock.getAttribute("principal")).thenReturn(createUserPrincipalWithRole(org.tiatus.role.Role.TIMING));
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(0)).sendRedirect(anyString());
    // }

    // @Test
    // public void testInvalidUserAccess() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/timing/timing.html");
    //     when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
    //     when(httpSessionMock.getAttribute("principal")).thenReturn(createUserPrincipalWithRole(org.tiatus.role.Role.ADJUDICATOR));
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(1)).sendRedirect("/login");
    // }

    // @Test
    // public void testNotLoggedIn() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/timing/timing.html");
    //     when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
    //     when(httpSessionMock.getAttribute("principal")).thenReturn(null);
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(1)).sendRedirect("/login");
    // }

    // @Test
    // public void testAccessSetupAfterSetup() throws Exception {
    //     when(httpServletRequestMock.getRequestURI()).thenReturn("/setup");
    //     when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
    //     when(httpSessionMock.getAttribute("principal")).thenReturn(createUserPrincipalWithRole(org.tiatus.role.Role.ADMIN));
    //     when(userServiceMock.hasAdminUser()).thenReturn(true);
    //     when(filterConfigMock.getInitParameter("pass-through")).thenReturn("xxxx");

    //     filter.setUserService(userServiceMock);
    //     filter.init(filterConfigMock);
    //     filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);
    //     filter.destroy();

    //     verify(httpServletResponseMock, times(1)).sendRedirect("/login");
    // }

    // private UserPrincipal createUserPrincipalWithRole(String roleString) {
    //     UserPrincipal userPrincipal = new UserPrincipal();
    //     User user = new User();
    //     user.setUserName("user");

    //     if (null != roleString) {
    //         UserRole userRole = new UserRole();
    //         Role role = new Role();
    //         role.setRoleName(roleString);
    //         userRole.setRole(role);
    //         userRole.setUser(user);
    //         Set<UserRole> userRoleList = new HashSet<>();
    //         userRoleList.add(userRole);
    //         user.setRoles(userRoleList);
    //     }

    //     userPrincipal.setUser(user);
    //     return userPrincipal;
    // }
}
