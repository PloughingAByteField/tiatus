package org.tiatus.server.rest;

// import org.junit.Assert;
// import org.junit.Before;
// import org.junit.Test;
// import org.tiatus.entity.User;
// import org.tiatus.service.ConfigServiceImpl;
// import org.tiatus.service.ServiceException;
// import org.tiatus.service.UserServiceImpl;

// import javax.servlet.ServletContext;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpSession;
// import javax.ws.rs.core.Response;
// import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by johnreynolds on 13/09/2016.
 */
public class SetupRestPointTest {
    // private HttpSession session;
    // private HttpServletRequest servletRequest;
    // private ServletContext context;

    // @Before
    // public void setup() {
    //     new MockUp<SetupRestPoint>() {
    //         @Mock
    //         void $init(Invocation invocation) {
    //             SetupRestPoint restPoint = invocation.getInvokedInstance();
    //             UserServiceImpl userService = new UserServiceImpl(null, null);
    //             ConfigServiceImpl configService = new ConfigServiceImpl(null);
    //             Deencapsulation.setField(restPoint, "userService", userService);
    //             Deencapsulation.setField(restPoint, "configService", configService);

    //         }
    //     };
    //     session = new MockUp<HttpSession>() {
    //         @Mock
    //         public String getId() {
    //             return "id";
    //         }
    //     }.getMockInstance();

    //     servletRequest = new MockUp<HttpServletRequest>() {
    //         @Mock
    //         public HttpSession getSession() {
    //             return session;
    //         }
    //     }.getMockInstance();

    //     context = new MockUp<ServletContext>() {
    //         @Mock
    //         public InputStream getResourceAsStream(String path) {
    //             return new InputStream() {
    //                 @Override
    //                 public int read() throws IOException {
    //                     return 0;
    //                 }
    //             };
    //         }
    //     }.getMockInstance();
    // }

    // @Test
    // public void testAddUser() throws Exception {
    //     UriInfo uriInfo = new MockUriInfo().getMockInstance();
    //     User user = new User();
    //     new MockUp<ConfigServiceImpl>() {
    //         @Mock
    //         public void setEventTitle(String title) throws ServiceException {}

    //         @Mock
    //         public String setEventLogo(InputStream stream, String fileName) throws ServiceException {
    //             return "logo";
    //         }
    //     };

    //     new MockUp<UserServiceImpl>() {
    //         @Mock
    //         public User addAdminUser(User user, String sessionId) throws ServiceException {
    //             user.setId(1L);
    //             return user;
    //         }

    //         @Mock
    //         public boolean hasAdminUser() { return false; }
    //     };
    //     SetupRestPoint setupRestPoint = new SetupRestPoint();

    //     Response response = setupRestPoint.addUser(uriInfo, servletRequest, context, user);
    //     Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    //     Assert.assertEquals(response.getLocation(), new URI("https://127.0.0.1:8080/rest/setup/1"));
    // }


    // @Test
    // public void testAddUserServiceException() throws Exception {
    //     UriInfo uriInfo = new MockUriInfo().getMockInstance();
    //     User user = new User();

    //     new MockUp<UserServiceImpl>() {
    //         @Mock
    //         public User addAdminUser(User user, String sessionId) throws ServiceException {
    //             throw new ServiceException("e");
    //         }

    //         @Mock
    //         public boolean hasAdminUser() { return false; }
    //     };
    //     SetupRestPoint setupRestPoint = new SetupRestPoint();

    //     Response response = setupRestPoint.addUser(uriInfo, servletRequest, context, user);
    //     Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    // }

    // @Test
    // public void testAddUserException() throws Exception {
    //     UriInfo uriInfo = new MockUriInfo(){
    //         @Mock
    //         public String getPath() {
    //             throw new RuntimeException();
    //         }
    //     }.getMockInstance();
    //     User user = new User();

    //     new MockUp<UserServiceImpl>() {
    //         @Mock
    //         public User addAdminUser(User user, String sessionId) throws ServiceException {
    //             user.setId(1L);
    //             return user;
    //         }

    //         @Mock
    //         public boolean hasAdminUser() { return false; }
    //     };
    //     SetupRestPoint setupRestPoint = new SetupRestPoint();

    //     setupRestPoint.addUser(uriInfo, servletRequest, context, user);
    // }

    // @Test
    // public void testSetUserService() throws Exception {
    //     SetupRestPoint setupRestPoint = new SetupRestPoint();
    //     setupRestPoint.setUserService(null);
    // }

    // class MockUriInfo extends MockUp<UriInfo> {
    //     @Mock
    //     public String getPath() {
    //         return "https://127.0.0.1:8080/rest/setup";
    //     }
    // }
}
