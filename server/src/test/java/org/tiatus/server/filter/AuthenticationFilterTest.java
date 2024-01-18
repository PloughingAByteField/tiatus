package org.tiatus.server.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.User;
import org.tiatus.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;

/**
 * Created by johnreynolds on 12/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    private AuthenticationFilter filter;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private ContainerRequestContext containerRequestContextMock;

    @Mock
    private UserServiceImpl userServiceMock;

    @Mock
    private UriInfo uriInfoMock;

    @Mock
    private UserPrincipal userPrincipalMock;

    @Mock
    private InputStream inputStreamMock;

    @Mock
    private Providers providersMock;

    @Mock
    private MessageBodyReader messageBodyReaderMock;

    @BeforeEach
    public void setup() throws Exception {
        filter = new AuthenticationFilter();

        Field field = ReflectionUtils
        .findFields(AuthenticationFilter.class, f -> f.getName().equals("servletRequest"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
        .get(0);
        field.setAccessible(true);
        field.set(filter, httpServletRequestMock);
    }

    @Test
    public void testLogInUser() throws Exception  {
        when(containerRequestContextMock.hasEntity()).thenReturn(true);
        when(containerRequestContextMock.getMediaType()).thenReturn(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        when(httpServletRequestMock.getSession(anyBoolean())).thenReturn(httpSessionMock);
        when(containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);
        when(uriInfoMock.getRequestUri()).thenReturn(new URI("https://127.0.0.1"));
        when(containerRequestContextMock.getEntityStream()).thenReturn(inputStreamMock);
        when(inputStreamMock.read(any())).thenReturn(-1);

        Form form = new Form();
        form = form.param("user", "user");
        form = form.param("pwd", "pwd");
        when(providersMock.getMessageBodyReader(any(), any(), any(), any())).thenReturn(messageBodyReaderMock);
        when(messageBodyReaderMock.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(form);

        Field field = ReflectionUtils
        .findFields(AuthenticationFilter.class, f -> f.getName().equals("providers"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
        .get(0);
        field.setAccessible(true);
        field.set(filter, providersMock);

        User user = new User();
        when(userServiceMock.getUser(anyString(), anyString())).thenReturn(user);

        filter.setUserService(userServiceMock);
        filter.filter(containerRequestContextMock);

        verify(containerRequestContextMock, times(1)).setSecurityContext(any());
    }

    @Test
    public void testNotAForm() throws Exception {
        when(containerRequestContextMock.getMediaType()).thenReturn(MediaType.APPLICATION_JSON_TYPE);
        when(containerRequestContextMock.hasEntity()).thenReturn(true);
        when(httpServletRequestMock.getSession(anyBoolean())).thenReturn(httpSessionMock);
        when(containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);
        when(uriInfoMock.getRequestUri()).thenReturn(new URI("https://127.0.0.1"));

        filter.filter(containerRequestContextMock);

        verify(containerRequestContextMock, times(0)).setSecurityContext(any());
    }

    @Test
    public void testNotHasEntity() throws Exception {
        when(containerRequestContextMock.hasEntity()).thenReturn(false);
        when(httpServletRequestMock.getSession(anyBoolean())).thenReturn(httpSessionMock);
        when(containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);
        when(uriInfoMock.getRequestUri()).thenReturn(new URI("https://127.0.0.1"));

        filter.filter(containerRequestContextMock);

        verify(containerRequestContextMock, times(0)).setSecurityContext(any());
    }

    @Test
    public void testLoggedInUser() throws Exception {
        when(httpServletRequestMock.getSession(anyBoolean())).thenReturn(httpSessionMock);
        when(containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);
        when(uriInfoMock.getRequestUri()).thenReturn(new URI("https://127.0.0.1"));
        when(httpSessionMock.getAttribute("principal")).thenReturn(userPrincipalMock);

        filter.setUserService(userServiceMock);
        filter.filter(containerRequestContextMock);

        verify(containerRequestContextMock, times(1)).setSecurityContext(any());
    }

    @Test
    public void testNoSuchUser() throws Exception {
        when(containerRequestContextMock.hasEntity()).thenReturn(true);
        when(containerRequestContextMock.getMediaType()).thenReturn(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        when(httpServletRequestMock.getSession(anyBoolean())).thenReturn(httpSessionMock);
        when(containerRequestContextMock.getUriInfo()).thenReturn(uriInfoMock);
        when(uriInfoMock.getRequestUri()).thenReturn(new URI("https://127.0.0.1"));
        when(containerRequestContextMock.getEntityStream()).thenReturn(inputStreamMock);
        when(inputStreamMock.read(any())).thenReturn(-1);

        Form form = new Form();
        form = form.param("user", "user");
        form = form.param("pwd", "pwd");
        when(providersMock.getMessageBodyReader(any(), any(), any(), any())).thenReturn(messageBodyReaderMock);
        when(messageBodyReaderMock.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(form);

        Field field = ReflectionUtils
        .findFields(AuthenticationFilter.class, f -> f.getName().equals("providers"),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
        .get(0);
        field.setAccessible(true);
        field.set(filter, providersMock);

        when(userServiceMock.getUser(anyString(), anyString())).thenReturn(null);

        filter.setUserService(userServiceMock);
        filter.filter(containerRequestContextMock);

        verify(containerRequestContextMock, times(0)).setSecurityContext(any());
    }

    @Test
    public void testSetUserService() {
        filter.setUserService(null);
    }
}
