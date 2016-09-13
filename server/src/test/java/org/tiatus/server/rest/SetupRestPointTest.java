package org.tiatus.server.rest;

import mockit.Deencapsulation;
import mockit.Invocation;
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
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 13/09/2016.
 */
public class SetupRestPointTest {

    @Before
    public void setup() {
        new MockUp<SetupRestPoint>() {
            @Mock
            void $init(Invocation invocation) {
                SetupRestPoint restPoint = invocation.getInvokedInstance();
                UserServiceImpl service = new UserServiceImpl(null);
                Deencapsulation.setField(restPoint, "service", service);
            }
        };
    }

    @Test
    public void testAddUser() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();
        User user = new User();

        new MockUp<UserServiceImpl>() {
            @Mock
            public void addUser(User user) throws ServiceException {
                user.setId(1L);
            }
        };
        SetupRestPoint setupRestPoint = new SetupRestPoint();

        Response response = setupRestPoint.addUser(uriInfo, user);
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Assert.assertEquals(response.getLocation(), new URI("https://127.0.0.1:8080/rest/setup/1"));
    }


    @Test (expected = InternalServerErrorException.class)
    public void testAddUserServiceException() throws Exception {
        UriInfo uriInfo = new MockUriInfo().getMockInstance();
        User user = new User();

        new MockUp<UserServiceImpl>() {
            @Mock
            public void addUser(User user) throws ServiceException {
                throw new ServiceException("e");
            }
        };
        SetupRestPoint setupRestPoint = new SetupRestPoint();

        setupRestPoint.addUser(uriInfo, user);
    }

    @Test (expected = InternalServerErrorException.class)
    public void testAddUserException() throws Exception {
        UriInfo uriInfo = new MockUriInfo(){
            @Mock
            public String getPath() {
                throw new RuntimeException();
            }
        }.getMockInstance();
        User user = new User();

        new MockUp<UserServiceImpl>() {
            @Mock
            public void addUser(User user) throws ServiceException {
                user.setId(1L);
            }
        };
        SetupRestPoint setupRestPoint = new SetupRestPoint();

        setupRestPoint.addUser(uriInfo, user);
    }

    @Test
    public void testSetService() throws Exception {
        SetupRestPoint setupRestPoint = new SetupRestPoint();
        setupRestPoint.setService(null);
    }

    class MockUriInfo extends MockUp<UriInfo> {
        @Mock
        public String getPath() {
            return "https://127.0.0.1:8080/rest/setup";
        }
    }
}
