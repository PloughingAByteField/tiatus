package org.tiatus.server.rest;

import mockit.Deencapsulation;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.jboss.resteasy.client.exception.mapper.ClientExceptionMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.entity.Race;
import org.tiatus.service.RaceService;
import org.tiatus.service.RaceServiceImpl;
import org.tiatus.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;


/**
 * Created by johnreynolds on 09/07/2016.
 */
public class RaceRestPointTest {

    RaceServiceImpl service = null;

    @Before
    public void setup() {
        new MockUp<RaceRestPoint>() {
            @Mock
            void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
                RaceRestPoint restPoint = invocation.getInvokedInstance();
                RaceServiceImpl service = new RaceServiceImpl(null);
                Deencapsulation.setField(restPoint, "service", service);
            }
        };
    }

    @Test
    public void addRace() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public Race addRace(Race race) throws ServiceException {
                System.out.println("in mock");
                return race;
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";
        Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory noDefaults = new POJOResourceFactory(RaceRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);

        MockHttpRequest request = MockHttpRequest.post("race");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void getRaces() throws Exception {
        Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory noDefaults = new POJOResourceFactory(RaceRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);

        MockHttpRequest request = MockHttpRequest.get("race");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
