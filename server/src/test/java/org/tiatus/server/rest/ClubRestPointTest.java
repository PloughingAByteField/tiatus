package org.tiatus.server.rest;

import mockit.Deencapsulation;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.infinispan.Cache;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.entity.Club;
import org.tiatus.role.Role;
import org.tiatus.service.ClubServiceImpl;
import org.tiatus.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnreynolds on 09/07/2016.
 */
public class ClubRestPointTest extends RestTestBase {

    @Before
    public void setup() throws Exception {
        new MockUp<ClubRestPoint>() {
            @Mock
            void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
                ClubRestPoint restPoint = invocation.getInvokedInstance();
                ClubServiceImpl service = new ClubServiceImpl(null, null);
                Deencapsulation.setField(restPoint, "service", service);
                Cache cache = new StubbedCache();
                Deencapsulation.setField(restPoint, "cache", cache);
            }
        };

        dispatcher = MockDispatcherFactory.createDispatcher();
        endPoint = new POJOResourceFactory(ClubRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(endPoint);
        endPointDetails = fillEndPointDetails(endPoint);
        HttpSession session = new MockUp<HttpSession>() {
            @Mock
            public String getId() {
                return "id";
            }
        }.getMockInstance();

        HttpServletRequest servletRequest = new MockUp<HttpServletRequest>() {
            @Mock
            public HttpSession getSession() {
                return session;
            }
        }.getMockInstance();
        dispatcher.getDefaultContextObjects().put(HttpServletRequest.class, servletRequest);
    }


    @Test
    public void addClub() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club addClub(Club club, String sessionId) throws ServiceException {
                return club;
            }
        };

        String payload = "{\"id\":\"1\",\"clubName\":\"Club 1\"}";

        MockHttpRequest request = MockHttpRequest.post("clubs");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void addClubServiceException() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club addClub(Club club, String sessionId) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };

        String payload = "{\"id\":\"1\",\"clubName\":\"Club 1\"}";

        MockHttpRequest request = MockHttpRequest.post("clubs");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void addClubGeneralException() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club addClub(Club club, String sessionId) throws Exception {
                throw new Exception("exception");
            }
        };

        String payload = "{\"id\":\"1\",\"clubName\":\"Club 1\"}";

        MockHttpRequest request = MockHttpRequest.post("clubs");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteClub() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club getClubForId(Long id) {
                Club club = new Club();
                club.setId(Long.valueOf(1));
                return club;
            }

            @Mock
            public void deleteClub(Club club, String sessionId) throws ServiceException {
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("clubs/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void deleteClubServiceException() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club getClubForId(Long id) {
                Club club = new Club();
                club.setId(Long.valueOf(1));
                return club;
            }

            @Mock
            public void deleteClub(Club club, String sessionId) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };


        MockHttpRequest request = MockHttpRequest.delete("clubs/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteClubGeneralException() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public void deleteClub(Club club, String sessionId) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("clubs/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updateClub() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club getClubForId(Long id) {
                Club club = new Club();
                club.setId(Long.valueOf(1));
                return club;
            }

            @Mock
            public void updateClub(Club club, String sessionId) throws ServiceException {
            }
        };

        MockHttpRequest request = MockHttpRequest.put("clubs/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        String payload = "{\"id\":\"1\",\"clubName\":\"Club 1\"}";
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void updateClubServiceException() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club getClubForId(Long id) {
                Club club = new Club();
                club.setId(Long.valueOf(1));
                return club;
            }

            @Mock
            public void updateClub(Club club, String sessionId) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };


        MockHttpRequest request = MockHttpRequest.put("clubs/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        String payload = "{\"id\":\"1\",\"clubName\":\"Club 1\"}";
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updateClubGeneralException() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public Club getClubForId(Long id) {
                Club club = new Club();
                club.setId(Long.valueOf(1));
                return club;
            }

            @Mock
            public void updateClub(Club club, String sessionId) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.put("clubs/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        String payload = "{\"id\":\"1\",\"clubName\":\"Club 1\"}";
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void getClubs() throws Exception {
        new MockUp<ClubServiceImpl>() {
            @Mock
            public List<Club> getClubs() {
                List<Club> clubs = new ArrayList<>();
                Club club = new Club();
                club.setId(1L);
                club.setClubName("Club 1");
                clubs.add(club);
                return clubs;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("clubs");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void checkGetClubAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:club");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:club is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getClubs")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }

    @Test
    public void checkAddClubAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs", "POST");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for POST:club");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for POST:club is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("addClub")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void checkDeleteClubAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs/{id}", "DELETE");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for DELETE:club");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for DELETE:club is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("removeClub")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void checkUpdateClubAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs/{id}", "PUT");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for PUT:club");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for PUT:club is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("updateClub")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }
}
