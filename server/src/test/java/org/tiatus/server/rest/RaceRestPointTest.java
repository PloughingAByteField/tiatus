package org.tiatus.server.rest;

import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.RaceServiceImpl;
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
public class RaceRestPointTest extends RestTestBase {

    // @Before
    // public void setup() throws Exception {
    //     new MockUp<RaceRestPoint>() {
    //         @Mock
    //         void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
    //             RaceRestPoint restPoint = invocation.getInvokedInstance();
    //             RaceServiceImpl service = new RaceServiceImpl(null, null);
    //             Deencapsulation.setField(restPoint, "service", service);
    //         }
    //     };

    //     dispatcher = MockDispatcherFactory.createDispatcher();
    //     endPoint = new POJOResourceFactory(RaceRestPoint.class);
    //     dispatcher.getRegistry().addResourceFactory(endPoint);
    //     endPointDetails = fillEndPointDetails(endPoint);
    //     HttpSession session = new MockUp<HttpSession>() {
    //         @Mock
    //         public String getId() {
    //             return "id";
    //         }
    //     }.getMockInstance();

    //     HttpServletRequest servletRequest = new MockUp<HttpServletRequest>() {
    //         @Mock
    //         public HttpSession getSession() {
    //             return session;
    //         }
    //     }.getMockInstance();
    //     dispatcher.getDefaultContextObjects().put(HttpServletRequest.class, servletRequest);
    // }


    // @Test
    // public void addRace() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         public Race addRace(Race race, String sessionId) throws ServiceException {
    //             return race;
    //         }
    //     };


    //     String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";

    //     MockHttpRequest request = MockHttpRequest.post("races");
    //     request.accept(MediaType.APPLICATION_JSON);
    //     request.contentType(MediaType.APPLICATION_JSON);
    //     request.content(payload.getBytes());

    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);

    //     Assert.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    // }

    // @Test
    // public void addRaceServiceException() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         public Race addRace(Race race, String sessionId) throws ServiceException {
    //             throw new ServiceException(new Exception("exception"));
    //         }
    //     };

    //     String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";

    //     MockHttpRequest request = MockHttpRequest.post("races");
    //     request.accept(MediaType.APPLICATION_JSON);
    //     request.contentType(MediaType.APPLICATION_JSON);
    //     request.content(payload.getBytes());

    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);

    //     Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    // }

    // @Test
    // public void addRaceGeneralException() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         public Race addRace(Race race, String sessionId) throws Exception {
    //             throw new Exception("exception");
    //         }
    //     };

    //     String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";

    //     MockHttpRequest request = MockHttpRequest.post("races");
    //     request.accept(MediaType.APPLICATION_JSON);
    //     request.contentType(MediaType.APPLICATION_JSON);
    //     request.content(payload.getBytes());

    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);

    //     Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    // }

    // @Test
    // public void deleteRace() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         Race getRaceForId(Long id) {
    //             Race race = new Race();
    //             race.setId(Long.valueOf(1));
    //             return race;
    //         }

    //         @Mock
    //         public void deleteRace(Race race, String sessionId) throws ServiceException {
    //         }
    //     };

    //     MockHttpRequest request = MockHttpRequest.delete("races/1");
    //     request.accept(MediaType.APPLICATION_JSON);
    //     request.contentType(MediaType.APPLICATION_JSON);

    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);

    //     Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    // }

    // @Test
    // public void deleteRaceServiceException() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         public void deleteRace(Race race, String sessionId) throws ServiceException {
    //             throw new ServiceException(new Exception("exception"));
    //         }
    //     };


    //     MockHttpRequest request = MockHttpRequest.delete("races/1");
    //     request.accept(MediaType.APPLICATION_JSON);
    //     request.contentType(MediaType.APPLICATION_JSON);

    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);

    //     Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    // }

    // @Test
    // public void deleteRaceGeneralException() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         public void deleteRace(Race race, String sessionId) throws Exception {
    //             throw new Exception("exception");
    //         }
    //     };

    //     MockHttpRequest request = MockHttpRequest.delete("races/1");
    //     request.accept(MediaType.APPLICATION_JSON);
    //     request.contentType(MediaType.APPLICATION_JSON);

    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);

    //     Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    // }

    // @Test
    // public void getRaces() throws Exception {
    //     new MockUp<RaceServiceImpl>() {
    //         @Mock
    //         public List<Race> getRaces() {
    //             List<Race> races = new ArrayList<>();
    //             Race race = new Race();
    //             race.setId(1L);
    //             race.setRaceOrder(1);
    //             race.setName("Race 1");
    //             races.add(race);
    //             return races;
    //         }
    //     };

    //     MockHttpRequest request = MockHttpRequest.get("races");
    //     MockHttpResponse response = new MockHttpResponse();
    //     dispatcher.invoke(request, response);
    //     Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    // }

    // @Test
    // public void checkGetRaceAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "races", "GET");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for GET:race");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for GET:race is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("getRaces")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
    //         System.out.println("End point is not allowed all");
    //         throw new Exception();
    //     }
    // }

    // @Test
    // public void checkAddRaceAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "races", "POST");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for POST:race");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for POST:race is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("addRace")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
    //         System.out.println("End point does not have expected roles");
    //         throw new Exception();
    //     }
    // }

    // @Test
    // public void checkDeleteRaceAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "races/{id}", "DELETE");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for DELETE:race");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for DELETE:race is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("removeRace")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
    //         System.out.println("End point does not have expected roles");
    //         throw new Exception();
    //     }
    // }
}
