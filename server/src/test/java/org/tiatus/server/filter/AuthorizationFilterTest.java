package org.tiatus.server.filter;

import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Created by johnreynolds on 12/09/2016.
 */
public class AuthorizationFilterTest {

    private AuthorizationFilter filter;

    @Before
    public void setup() {
        new MockUp<AuthorizationFilter>() {
        };

        filter = new AuthorizationFilter();
    }

    @Test
    public void testRoleAllowed() throws Exception {
        ResourceInfo resourceInfo = new MockUp<ResourceInfo>() {
            @Mock
            public Method getResourceMethod() {
                return new MockUp<Method>(){
                    @Mock
                    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
                        if (RolesAllowed.class.equals(annotationClass)) {
                            return true;
                        }

                        return false;
                    }

                    @Mock
                    public RolesAllowed getAnnotation(Class<RolesAllowed> annotationClass) {
                        return new MockUp<RolesAllowed>(){
                            @Mock
                            public String[] value() {
                                return new String[]{"ADMIN"};
                            }
                        }.getMockInstance();
                    }
                }.getMockInstance();
            }
        }.getMockInstance();
        Deencapsulation.setField(filter, "resourceInfo", resourceInfo);

        ContainerRequestContext requestContext = new MockContainerRequestContext().getMockInstance();
        filter.filter(requestContext);
    }

    @Test
    public void testRoleAllowedNotFound() throws Exception {
        ResourceInfo resourceInfo = new MockUp<ResourceInfo>() {
            @Mock
            public Method getResourceMethod() {
                return new MockUp<Method>(){
                    @Mock
                    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
                        if (RolesAllowed.class.equals(annotationClass)) {
                            return true;
                        }

                        return false;
                    }

                    @Mock
                    public RolesAllowed getAnnotation(Class<RolesAllowed> annotationClass) {
                        return new MockUp<RolesAllowed>(){
                            @Mock
                            public String[] value() {
                                return new String[]{"BAD"};
                            }
                        }.getMockInstance();
                    }
                }.getMockInstance();
            }
        }.getMockInstance();
        Deencapsulation.setField(filter, "resourceInfo", resourceInfo);

        ContainerRequestContext requestContext = new MockContainerRequestContext(){
            @Mock
            public UriInfo getUriInfo() {
                return new MockUp<UriInfo>() {
                    @Mock
                    public URI getRequestUri() {
                        try {
                            return new URI("https://127.0.0.1/");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();
        try {
            filter.filter(requestContext);
        } catch (WebApplicationException e) {
            if (! (e.getResponse().getStatus() == Response.Status.FORBIDDEN.getStatusCode())) {
                fail("Got unexpected code " + e.getResponse().getStatus());
            }
        }

    }

    @Test
    public void testDenyAll() throws Exception {
        ResourceInfo resourceInfo = new MockUp<ResourceInfo>() {
            @Mock
            public Method getResourceMethod() {
                return new MockUp<Method>(){
                    @Mock
                    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
                        if (DenyAll.class.equals(annotationClass)) {
                            return true;
                        }

                        return false;
                    }
                }.getMockInstance();
            }
        }.getMockInstance();
        Deencapsulation.setField(filter, "resourceInfo", resourceInfo);

        ContainerRequestContext requestContext = new MockContainerRequestContext().getMockInstance();
        try {
            filter.filter(requestContext);
        } catch (WebApplicationException e) {
            if (! (e.getResponse().getStatus() == Response.Status.FORBIDDEN.getStatusCode())) {
                fail("Got unexpected code " + e.getResponse().getStatus());
            }
        }
    }

    @Test
    public void testNullUserPrincipal() throws Exception {

        ContainerRequestContext requestContext = new MockContainerRequestContext(){
            @Override
            @Mock
            public SecurityContext getSecurityContext() {
                return new TiatusSecurityContext(null, "https");
            }
        }.getMockInstance();

        filter.filter(requestContext);
    }

    class MockContainerRequestContext extends MockUp<ContainerRequestContext> {
        @Mock
        public SecurityContext getSecurityContext() {
            UserPrincipal principal = new UserPrincipal();
            User user = new User();
            user.setUserName("user");

            UserRole userRole = new UserRole();
            Role role = new Role();
            role.setRoleName(org.tiatus.role.Role.ADMIN);
            userRole.setRole(role);
            userRole.setUser(user);
            Set<UserRole> userRoleList = new HashSet<>();
            userRoleList.add(userRole);
            user.setRoles(userRoleList);
            principal.setUser(user);
            return new TiatusSecurityContext(principal, "https");
        }
    }
}
