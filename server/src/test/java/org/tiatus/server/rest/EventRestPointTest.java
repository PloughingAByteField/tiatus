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
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;
import org.tiatus.role.Role;
import org.tiatus.service.EventServiceImpl;
import org.tiatus.service.ServiceException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnreynolds on 09/07/2016.
 */
public class EventRestPointTest extends RestTestBase {

    @Before
    public void setup() throws Exception {
        new MockUp<EventRestPoint>() {
            @Mock
            void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
                EventRestPoint restPoint = invocation.getInvokedInstance();
                EventServiceImpl service = new EventServiceImpl(null, null);
                Deencapsulation.setField(restPoint, "service", service);
            }
        };

        dispatcher = MockDispatcherFactory.createDispatcher();
        endPoint = new POJOResourceFactory(EventRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(endPoint);
        endPointDetails = fillEndPointDetails(endPoint);
    }

    @Test
    public void addEvent() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public Event addEvent(Event event) throws ServiceException {
                return event;
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Event 1\"}";

        MockHttpRequest request = MockHttpRequest.post("events");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void addEventServiceException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public Event addEvent(Event event) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Event 1\"}";

        MockHttpRequest request = MockHttpRequest.post("events");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void addEventGeneralException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public Event addEvent(Event event) throws Exception {
                throw new Exception("exception");
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Event 1\"}";

        MockHttpRequest request = MockHttpRequest.post("events");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteEvent() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void deleteEvent(Event event) throws ServiceException {
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("events/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void deleteEventServiceException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void deleteEvent(Event event) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };


        MockHttpRequest request = MockHttpRequest.delete("events/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteEventGeneralException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void deleteEvent(Event event) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("events/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void getEvents() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public List<Event> getEvents() {
                List<Event> events = new ArrayList<>();
                Event event = new Event();
                event.setId(1L);
                event.setName("name");
                event.setWeighted(true);
                events.add(event);
                return events;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("events");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void getUnassignedEvents() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public List<Event> getUnassignedEvents() {
                List<Event> events = new ArrayList<>();
                Event event = new Event();
                event.setId(1L);
                event.setName("name");
                event.setWeighted(true);
                events.add(event);
                return events;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("events/unassigned");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void getAssignedEvents() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public List<RaceEvent> getAssignedEvents() {
                List<RaceEvent> raceEvents = new ArrayList<>();
                Race race1 = new Race();
                race1.setId(1L);
                race1.setName("Race 1");
                Event event1 = new Event();
                event1.setId(1L);
                event1.setName("Event 1");
                RaceEvent raceEvent1 = new RaceEvent();
                raceEvent1.setId(1L);
                raceEvent1.setRace(race1);
                raceEvent1.setEvent(event1);
                raceEvents.add(raceEvent1);
                return raceEvents;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("events/assigned");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void deleteAssignedEvent() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void deleteRaceEvent(RaceEvent raceEvent) {
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("events/assigned/1");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void deleteAssignedEventGeneralException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void deleteRaceEvent(RaceEvent raceEvent) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("events/assigned/1");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteAssignedEventServiceException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void deleteRaceEvent(RaceEvent raceEvent) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("events/assigned/1");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }


    @Test
    public void addAssignedEvent() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public RaceEvent addRaceEvent(RaceEvent raceEvent) throws ServiceException {
                return raceEvent;
            }
        };

        String payload = "{\"raceEventOrder\": \"1\",\"event\": {\"name\":\"Event 1\"}, \"race\": {\"id\": \"1\", \"name\": \"Race 1\"}}";

        MockHttpRequest request = MockHttpRequest.post("events/assigned");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void addAssignedEventGeneralException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public RaceEvent addRaceEvent(RaceEvent raceEvent) throws Exception {
                throw new Exception("exception");
            }
        };

        String payload = "{\"raceEventOrder\": \"1\",\"event\": {\"name\":\"Event 1\"}, \"race\": {\"id\": \"1\", \"name\": \"Race 1\"}}";

        MockHttpRequest request = MockHttpRequest.post("events/assigned");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void addAssignedEventServiceException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public RaceEvent addRaceEvent(RaceEvent raceEvent) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };

        String payload = "{\"raceEventOrder\": \"1\",\"event\": {\"name\":\"Event 1\"}, \"race\": {\"id\": \"1\", \"name\": \"Race 1\"}}";

        MockHttpRequest request = MockHttpRequest.post("events/assigned");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updateAssignedEvents() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void updateRaceEvents(List<RaceEvent> raceEvents) throws ServiceException {
            }
        };

        String payload = "[{\"raceEventOrder\": \"1\",\"event\": {\"name\":\"Event 1\"}, \"race\": {\"id\": \"1\", \"name\": \"Race 1\"}}]";

        MockHttpRequest request = MockHttpRequest.put("events/assigned");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void updateAssignedEventsServiceException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void updateRaceEvents(List<RaceEvent> raceEvents) throws ServiceException {
                throw new ServiceException("error");
            }
        };

        String payload = "[{\"raceEventOrder\": \"1\",\"event\": {\"name\":\"Event 1\"}, \"race\": {\"id\": \"1\", \"name\": \"Race 1\"}}]";

        MockHttpRequest request = MockHttpRequest.put("events/assigned");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updateAssignedEventsGeneralException() throws Exception {
        new MockUp<EventServiceImpl>() {
            @Mock
            public void updateRaceEvents(List<RaceEvent> raceEvents) throws Exception {
                throw new Exception();
            }
        };

        String payload = "[{\"raceEventOrder\": \"1\",\"event\": {\"name\":\"Event 1\"}, \"race\": {\"id\": \"1\", \"name\": \"Race 1\"}}]";

        MockHttpRequest request = MockHttpRequest.put("events/assigned");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void createEventAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events", "POST");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for POST:events");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for POST:events is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("createEvent")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void removeEventAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/{id}", "DELETE");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for DELETE:events");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for DELETE:events is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("removeEvent")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void getEventsAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/assigned", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:events/assigned");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:events/assigned is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getAssignedEvents")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }

    @Test
    public void getAssignedEventsAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/assigned", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:events/assigned");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:events/assigned is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getAssignedEvents")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }

    @Test
    public void removeAssignedEventAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/assigned/{id}", "DELETE");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for DELETE:events/assigned");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for DELETE:events/assigned is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("removeAssignedEvent")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void addAssignedEventAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/assigned", "POST");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for POST:events/assigned");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for POST:events/assigned is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("addAssignedEvent")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void updateAssignedEventsAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/assigned", "PUT");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for PUT:events/assigned");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for PUT:events/assigned is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("updateAssignedEvents")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void getUnassignedEventsAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "events/unassigned", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:events/unassigned");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:events/unassigned is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getUnassignedEvents")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }
}
