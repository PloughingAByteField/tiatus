package org.tiatus.server.rest;

// import org.junit.Assert;
// import org.junit.Test;

// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpSession;
// import javax.ws.rs.core.Response;
// import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by johnreynolds on 13/09/2016.
 */
public class LogoutRestPointTest {

    // @Test
    // public void testLogout() throws Exception {
    //     UriInfo uriInfo = new MockUriInfo().getMockInstance();
    //     HttpServletRequest httpServletRequest = new MockHttpServletRequest().getMockInstance();

    //     LogoutRestPoint logoutRestPoint = new LogoutRestPoint();

    //     Response response = logoutRestPoint.logout(uriInfo, httpServletRequest);
    //     Assert.assertEquals(Response.Status.TEMPORARY_REDIRECT.getStatusCode(), response.getStatus());
    // }

    // @Test
    // public void testLogoutNullSession() throws Exception {
    //     UriInfo uriInfo = new MockUriInfo().getMockInstance();
    //     HttpServletRequest httpServletRequest = new MockHttpServletRequest(){
    //         @Override
    //         public HttpSession getSession() {
    //             return null;
    //         }
    //     }.getMockInstance();

    //     LogoutRestPoint logoutRestPoint = new LogoutRestPoint();

    //     Response response = logoutRestPoint.logout(uriInfo, httpServletRequest);
    //     Assert.assertEquals(Response.Status.TEMPORARY_REDIRECT.getStatusCode(), response.getStatus());
    // }

    // @Test
    // public void testLogoutException() throws Exception {
    //     UriInfo uriInfo = new MockUriInfo().getMockInstance();

    //     HttpServletRequest httpServletRequest = new MockHttpServletRequest() {
    //         @Override
    //         @Mock
    //         public String getContextPath() {
    //             return null;
    //         }
    //     }.getMockInstance();

    //     LogoutRestPoint logoutRestPoint = new LogoutRestPoint();

    //     Response response = logoutRestPoint.logout(uriInfo, httpServletRequest);
    //     Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    // }

    // class MockHttpSession extends MockUp<HttpSession> {
    //     @Mock
    //     public void invalidate() {}
    // }

    // class MockUriInfo extends MockUp<UriInfo> {
    //     @Mock
    //     public URI getBaseUri() {
    //         try {
    //             return new URI("https://127.0.0.1:8080");
    //         } catch (URISyntaxException e) {
    //             e.printStackTrace();
    //         }
    //         return null;
    //     }
    // }

    // class MockHttpServletRequest extends MockUp<HttpServletRequest> {
    //     @Mock
    //     public String getContextPath() {
    //         return "/";
    //     }

    //     @Mock
    //     public HttpSession getSession() {
    //         return new MockHttpSession().getMockInstance();
    //     }
    // }
}
