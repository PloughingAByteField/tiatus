package org.tiatus.server.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// import javax.annotation.security.RolesAllowed;
// import javax.ws.rs.WebApplicationException;
// import javax.ws.rs.container.ContainerRequestContext;
// import javax.ws.rs.container.ResourceInfo;
// import javax.ws.rs.core.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by johnreynolds on 12/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class AuthorizationFilterTest {

    // private AuthorizationFilter filter;

    // @Mock
    // private ResourceInfo resourceInfoMock;

    // @Mock
    // private Method methodMock;

    // @Mock
    // private SecurityContext securityContextMock;

    // @Mock
    // private RolesAllowed rolesAllowedMock;

    // @Mock
    // private Principal userPrincipalMock;

    // @Mock
    // private ContainerRequestContext containerRequestContextMock;

    // @Mock
    // private UriInfo uriInfoMock;

    // @Mock
    // private URI uriMock;

    // @BeforeEach
    // public void setup() throws Exception {
    //     filter = new AuthorizationFilter();

    //     Field field = ReflectionUtils
    //     .findFields(AuthorizationFilter.class, f -> f.getName().equals("resourceInfo"),
    //         ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
    //     .get(0);
    //     field.setAccessible(true);
    //     field.set(filter, resourceInfoMock);
    // }

    // @Test
    // public void testRoleAllowed() throws Exception {
    //     when(resourceInfoMock.getResourceMethod()).thenReturn(methodMock);
    //     when(methodMock.isAnnotationPresent(any())).thenReturn(false).thenReturn(true);
    //     when(containerRequestContextMock.getSecurityContext()).thenReturn(securityContextMock);
    //     when(securityContextMock.getUserPrincipal()).thenReturn(userPrincipalMock);
    //     when(methodMock.getAnnotation(any())).thenReturn(rolesAllowedMock);
    //     when(rolesAllowedMock.value()).thenReturn(new String[]{"ADMIN"});
    //     when(securityContextMock.isUserInRole(any())).thenReturn(true);

    //     Assertions.assertDoesNotThrow(() -> filter.filter(containerRequestContextMock));
    // }

    // @Test
    // public void testRoleAllowedNotFound() throws Exception {
    //     when(resourceInfoMock.getResourceMethod()).thenReturn(methodMock);
    //     when(methodMock.isAnnotationPresent(any())).thenReturn(false).thenReturn(true);
    //     when(containerRequestContextMock.getSecurityContext()).thenReturn(securityContextMock);
    //     when(securityContextMock.getUserPrincipal()).thenReturn(userPrincipalMock);
    //     when(methodMock.getAnnotation(any())).thenReturn(rolesAllowedMock);
    //     when(rolesAllowedMock.value()).thenReturn(new String[]{"ADMIN"});
    //     when(securityContextMock.isUserInRole(any())).thenReturn(false);
    //     when(userPrincipalMock.getName()).thenReturn("name");
    //     when(containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);
    //     when(uriInfoMock.getRequestUri()).thenReturn(uriMock);
        
    //     WebApplicationException thrown = Assertions.assertThrows(WebApplicationException.class, () -> {
    //         filter.filter(containerRequestContextMock);
    //     });

    //     Assertions.assertEquals(Response.Status.FORBIDDEN, thrown.getResponse().getStatusInfo());
    // }

    // @Test
    // public void testDenyAll() throws Exception {
    //     when(resourceInfoMock.getResourceMethod()).thenReturn(methodMock);
    //     when(methodMock.isAnnotationPresent(any())).thenReturn(true);
    //     when(containerRequestContextMock.getSecurityContext()).thenReturn(securityContextMock);
        
    //     WebApplicationException thrown = Assertions.assertThrows(WebApplicationException.class, () -> {
    //         filter.filter(containerRequestContextMock);
    //     });

    //     Assertions.assertEquals(Response.Status.FORBIDDEN, thrown.getResponse().getStatusInfo());
    // }

    // @Test
    // public void testNullUserPrincipal() throws Exception {
    //     when(resourceInfoMock.getResourceMethod()).thenReturn(methodMock);
    //     when(methodMock.isAnnotationPresent(any())).thenReturn(false).thenReturn(true);
    //     when(containerRequestContextMock.getSecurityContext()).thenReturn(securityContextMock);
    //     when(securityContextMock.getUserPrincipal()).thenReturn(null);

    //     WebApplicationException thrown = Assertions.assertThrows(WebApplicationException.class, () -> {
    //         filter.filter(containerRequestContextMock);
    //     });

    //     Assertions.assertEquals(Response.Status.FORBIDDEN, thrown.getResponse().getStatusInfo());
    // }
}
