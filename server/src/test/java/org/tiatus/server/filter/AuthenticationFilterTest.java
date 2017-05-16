package org.tiatus.server.filter;

import mockit.Deencapsulation;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.User;
import org.tiatus.service.UserServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by johnreynolds on 12/09/2016.
 */
public class AuthenticationFilterTest {

    private AuthenticationFilter filter;

    @Before
    public void setup() {
        new MockUp<AuthenticationFilter>() {
            @Mock
            void $init(Invocation invocation) {
                AuthenticationFilter f = invocation.getInvokedInstance();

                HttpServletRequest servletRequest = new MockUp<HttpServletRequest>() {
                    @Mock
                    public HttpSession getSession() {
                        return new MockHttpSession().getMockInstance();
                    }
                }.getMockInstance();
                Deencapsulation.setField(f, "servletRequest", servletRequest);

                Providers providers = new MockUp<Providers>() {
                    @Mock
                    <T> MessageBodyReader<T> getMessageBodyReader(Class<T> type,
                                                                  Type genericType, Annotation[] annotations, MediaType mediaType) {
                        return new MockUp<MessageBodyReader<T>>() {
                            @Mock
                            public boolean isReadable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
                                return true;
                            }

                            @Mock
                            public Object readFrom(Class aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
                                Form form = new Form();
                                form = form.param("user", "user");
                                form = form.param("pwd", "pwd");
                                return form;
                            }

                        }.getMockInstance();
                    }

                }.getMockInstance();
                Deencapsulation.setField(f, "providers", providers);
            }
        };

        filter = new AuthenticationFilter();
    }

    class MockHttpSession extends MockUp<HttpSession> {
        @Mock
        public Object getAttribute(String name) {
            return null;
        }
    }

    class MockContainerRequestContext extends MockUp<ContainerRequestContext> {
        @Mock
        public boolean hasEntity() {
            return true;
        }

        @Mock
        public MediaType getMediaType() {
            return MediaType.APPLICATION_FORM_URLENCODED_TYPE;
        }

        @Mock
        public InputStream getEntityStream() {
            return new InputStream() {
                @Override
                public int read() throws IOException {
                    return -1;
                }
            };
        }

        @Mock
        public UriInfo getUriInfo() {
            return new UriInfo() {
                @Override
                public String getPath() {
                    return null;
                }

                @Override
                public String getPath(boolean decode) {
                    return null;
                }

                @Override
                public List<PathSegment> getPathSegments() {
                    return null;
                }

                @Override
                public List<PathSegment> getPathSegments(boolean decode) {
                    return null;
                }

                @Override
                public URI getRequestUri() {
                    try {
                        return new URI("https://127.0.0.1");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public UriBuilder getRequestUriBuilder() {
                    return null;
                }

                @Override
                public URI getAbsolutePath() {
                    return null;
                }

                @Override
                public UriBuilder getAbsolutePathBuilder() {
                    return null;
                }

                @Override
                public URI getBaseUri() {
                    return null;
                }

                @Override
                public UriBuilder getBaseUriBuilder() {
                    return null;
                }

                @Override
                public MultivaluedMap<String, String> getPathParameters() {
                    return null;
                }

                @Override
                public MultivaluedMap<String, String> getPathParameters(boolean decode) {
                    return null;
                }

                @Override
                public MultivaluedMap<String, String> getQueryParameters() {
                    return null;
                }

                @Override
                public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
                    return null;
                }

                @Override
                public List<String> getMatchedURIs() {
                    return null;
                }

                @Override
                public List<String> getMatchedURIs(boolean decode) {
                    return null;
                }

                @Override
                public List<Object> getMatchedResources() {
                    return null;
                }

                @Override
                public URI resolve(URI uri) {
                    return null;
                }

                @Override
                public URI relativize(URI uri) {
                    return null;
                }
            };
        }
    }

    @Test
    public void testLogInUser() throws Exception {
        ContainerRequestContext requestContext = new MockContainerRequestContext().getMockInstance();

        UserServiceImpl userService = new MockUp<UserServiceImpl>() {
            @Mock
            public User getUser(String userName, String password) {
                return new User();
            }
        }.getMockInstance();
        Deencapsulation.setField(filter, "userService", userService);

        filter.filter(requestContext);
    }

    @Test
    public void testNotAForm() throws Exception {
        ContainerRequestContext requestContext = new MockContainerRequestContext(){
            @Mock
            @Override
            public MediaType getMediaType() {
                return MediaType.APPLICATION_JSON_TYPE;
            }

        }.getMockInstance();

        filter.filter(requestContext);
    }

    @Test
    public void testNotHasEntity() throws Exception {
        ContainerRequestContext requestContext = new MockContainerRequestContext(){
            @Mock
            @Override
            public boolean hasEntity() {
                return false;
            }

        }.getMockInstance();

        filter.filter(requestContext);
    }

    @Test
    public void testLoggedInUser() throws Exception {
        ContainerRequestContext requestContext = new MockContainerRequestContext().getMockInstance();

        HttpSession session = new MockHttpSession(){
            @Override
            @Mock
            public Object getAttribute(String name) {
                return new UserPrincipal();
            }
        }.getMockInstance();

        HttpServletRequest servletRequest = new MockUp<HttpServletRequest>() {
            @Mock
            public HttpSession getSession(boolean create) {
                return session;
            }
        }.getMockInstance();
        Deencapsulation.setField(filter, "servletRequest", servletRequest);

        filter.filter(requestContext);
    }

    @Test
    public void testNullUser() throws Exception {
        ContainerRequestContext requestContext = new MockContainerRequestContext().getMockInstance();

        UserServiceImpl userService = new MockUp<UserServiceImpl>() {
            @Mock
            public User getUser(String userName, String password) {
                return null;
            }
        }.getMockInstance();
        Deencapsulation.setField(filter, "userService", userService);

        filter.filter(requestContext);
    }

    @Test
    public void testSetUserService() {
        filter.setUserService(null);
    }
}
