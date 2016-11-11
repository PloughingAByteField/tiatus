package org.tiatus.server.rest;

import mockit.Deencapsulation;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.entity.Position;
import org.tiatus.role.Role;
import org.tiatus.service.PositionServiceImpl;
import org.tiatus.service.ServiceException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnreynolds on 09/07/2016.
 */
public class PositionRestPointTest extends RestTestBase {

    @Before
    public void setup() throws Exception {
        new MockUp<PositionRestPoint>() {
            @Mock
            void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
                PositionRestPoint restPoint = invocation.getInvokedInstance();
                PositionServiceImpl service = new PositionServiceImpl(null);
                Deencapsulation.setField(restPoint, "service", service);
            }
        };

        dispatcher = MockDispatcherFactory.createDispatcher();
        endPoint = new POJOResourceFactory(PositionRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(endPoint);
        endPointDetails = fillEndPointDetails(endPoint);
    }


    @Test
    public void addPosition() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public Position addPosition(Position position) throws ServiceException {
                return position;
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Position 1\"}";

        MockHttpRequest request = MockHttpRequest.post("positions");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void addPositionServiceException() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public Position addPosition(Position position) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Position 1\"}";

        MockHttpRequest request = MockHttpRequest.post("positions");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void addPositionGeneralException() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public Position addPosition(Position position) throws Exception {
                throw new Exception("exception");
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Position 1\"}";

        MockHttpRequest request = MockHttpRequest.post("positions");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deletePosition() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public void removePosition(Position position) throws ServiceException {
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("positions/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void deletePositionServiceException() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public void removePosition(Position position) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };


        MockHttpRequest request = MockHttpRequest.delete("positions/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deletePositionGeneralException() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public void removePosition(Position position) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("positions/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updatePosition() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public void updatePosition(Position position) throws ServiceException {
            }
        };

        MockHttpRequest request = MockHttpRequest.put("positions");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        String payload = "{\"id\":\"1\",\"name\":\"Position 1\"}";
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void updatePositionServiceException() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public void updatePosition(Position position) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };


        MockHttpRequest request = MockHttpRequest.put("positions");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        String payload = "{\"id\":\"1\",\"name\":\"Position 1\"}";
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updatePositionGeneralException() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public void updatePosition(Position position) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.put("positions");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        String payload = "{\"id\":\"1\",\"name\":\"Position 1\"}";
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void getPositions() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public List<Position> getPositions() {
                List<Position> positions = new ArrayList<>();
                Position position = new Position();
                position.setId(1L);
                position.setOrder(1);
                position.setName("Position 1");
                positions.add(position);
                return positions;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("positions");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }


    @Test
    public void getActiveTimingPositions() throws Exception {
        new MockUp<PositionServiceImpl>() {
            @Mock
            public List<Position> getActiveTimingPositions() {
                List<Position> positions = new ArrayList<>();
                Position position = new Position();
                position.setId(1L);
                position.setOrder(1);
                position.setName("Position 1");
                positions.add(position);
                return positions;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("positions/activeTiming");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void checkGetPositionAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "positions", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:position");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:position is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getPositions")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void checkGetActiveTimingPositionsAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "positions/activeTiming", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:position");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:position is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getActiveTimingPositions")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }

    @Test
    public void checkAddPositionAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "positions", "POST");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for POST:position");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for POST:position is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("addPosition")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void checkDeletePositionAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "positions/{id}", "DELETE");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for DELETE:position");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for DELETE:position is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("removePosition")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void checkUpdatePositionAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "positions", "PUT");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for PUT:position");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for PUT:position is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("updatePosition")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }
}
