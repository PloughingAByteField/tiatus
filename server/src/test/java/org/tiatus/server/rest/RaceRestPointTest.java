package org.tiatus.server.rest;

import mockit.Deencapsulation;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.CacheCollection;
import org.infinispan.CacheSet;
import org.infinispan.commons.util.concurrent.NotifyingFuture;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.filter.KeyFilter;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachelistener.filter.CacheEventConverter;
import org.infinispan.notifications.cachelistener.filter.CacheEventFilter;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by johnreynolds on 09/07/2016.
 */
public class RaceRestPointTest extends RestTestBase {

    @Before
    public void setup() throws Exception {
        new MockUp<RaceRestPoint>() {
            @Mock
            void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
                RaceRestPoint restPoint = invocation.getInvokedInstance();
                RaceServiceImpl service = new RaceServiceImpl(null, null);
                Deencapsulation.setField(restPoint, "service", service);
                Cache cache = new Cache() {
                    @Override
                    public void putForExternalRead(Object o, Object o2) {

                    }

                    @Override
                    public void putForExternalRead(Object o, Object o2, long l, TimeUnit timeUnit) {

                    }

                    @Override
                    public void putForExternalRead(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {

                    }

                    @Override
                    public void evict(Object o) {

                    }

                    @Override
                    public Configuration getCacheConfiguration() {
                        return null;
                    }

                    @Override
                    public EmbeddedCacheManager getCacheManager() {
                        return null;
                    }

                    @Override
                    public AdvancedCache getAdvancedCache() {
                        return null;
                    }

                    @Override
                    public ComponentStatus getStatus() {
                        return null;
                    }

                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public CacheSet keySet() {
                        return null;
                    }

                    @Override
                    public CacheCollection values() {
                        return null;
                    }

                    @Override
                    public CacheSet<Entry> entrySet() {
                        return null;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public String getVersion() {
                        return null;
                    }

                    @Override
                    public Object put(Object o, Object o2) {
                        return null;
                    }

                    @Override
                    public Object put(Object o, Object o2, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public Object putIfAbsent(Object o, Object o2, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public void putAll(Map map, long l, TimeUnit timeUnit) {

                    }

                    @Override
                    public Object replace(Object o, Object o2, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public boolean replace(Object o, Object o2, Object v1, long l, TimeUnit timeUnit) {
                        return false;
                    }

                    @Override
                    public Object put(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public Object putIfAbsent(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public void putAll(Map map, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {

                    }

                    @Override
                    public Object replace(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public boolean replace(Object o, Object o2, Object v1, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return false;
                    }

                    @Override
                    public Object remove(Object o) {
                        return null;
                    }

                    @Override
                    public Object putIfAbsent(Object key, Object value) {
                        return null;
                    }

                    @Override
                    public boolean remove(Object key, Object value) {
                        return false;
                    }

                    @Override
                    public boolean replace(Object key, Object oldValue, Object newValue) {
                        return false;
                    }

                    @Override
                    public Object replace(Object key, Object value) {
                        return null;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean containsKey(Object key) {
                        return false;
                    }

                    @Override
                    public boolean containsValue(Object value) {
                        return false;
                    }

                    @Override
                    public Object get(Object key) {
                        return null;
                    }

                    @Override
                    public void putAll(Map m) {

                    }

                    @Override
                    public NotifyingFuture putAsync(Object o, Object o2) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture putAsync(Object o, Object o2, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture putAsync(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Void> putAllAsync(Map map) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Void> putAllAsync(Map map, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Void> putAllAsync(Map map, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Void> clearAsync() {
                        return null;
                    }

                    @Override
                    public NotifyingFuture putIfAbsentAsync(Object o, Object o2) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture putIfAbsentAsync(Object o, Object o2, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture putIfAbsentAsync(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture removeAsync(Object o) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Boolean> removeAsync(Object o, Object o1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture replaceAsync(Object o, Object o2) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture replaceAsync(Object o, Object o2, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture replaceAsync(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Boolean> replaceAsync(Object o, Object o2, Object v1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Boolean> replaceAsync(Object o, Object o2, Object v1, long l, TimeUnit timeUnit) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture<Boolean> replaceAsync(Object o, Object o2, Object v1, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
                        return null;
                    }

                    @Override
                    public NotifyingFuture getAsync(Object o) {
                        return null;
                    }

                    @Override
                    public boolean startBatch() {
                        return false;
                    }

                    @Override
                    public void endBatch(boolean b) {

                    }

                    @Override
                    public void start() {

                    }

                    @Override
                    public void stop() {

                    }

                    @Override
                    public void addListener(Object o, KeyFilter keyFilter) {

                    }

                    @Override
                    public void addListener(Object o, CacheEventFilter cacheEventFilter, CacheEventConverter cacheEventConverter) {

                    }

                    @Override
                    public void addFilteredListener(Object o, CacheEventFilter cacheEventFilter, CacheEventConverter cacheEventConverter, Set set) {

                    }

                    @Override
                    public void addListener(Object o) {

                    }

                    @Override
                    public void removeListener(Object o) {

                    }

                    @Override
                    public Set<Object> getListeners() {
                        return null;
                    }
                };
                Deencapsulation.setField(restPoint, "cache", cache);
            }
        };

        dispatcher = MockDispatcherFactory.createDispatcher();
        endPoint = new POJOResourceFactory(RaceRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(endPoint);
        endPointDetails = fillEndPointDetails(endPoint);
    }


    @Test
    public void addRace() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public Race addRace(Race race, String sessionId) throws ServiceException {
                return race;
            }
        };

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
        String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";

        MockHttpRequest request = MockHttpRequest.post("races");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.getDefaultContextObjects().put(HttpServletRequest.class, servletRequest);
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void addRaceServiceException() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public Race addRace(Race race, String sessionId) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";

        MockHttpRequest request = MockHttpRequest.post("races");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void addRaceGeneralException() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public Race addRace(Race race, String sessionId) throws Exception {
                throw new Exception("exception");
            }
        };

        String payload = "{\"id\":\"1\",\"name\":\"Race 1\"}";

        MockHttpRequest request = MockHttpRequest.post("races");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload.getBytes());

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteRace() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public void deleteRace(Race race) throws ServiceException {
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("races/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void deleteRaceServiceException() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public void deleteRace(Race race) throws ServiceException {
                throw new ServiceException(new Exception("exception"));
            }
        };


        MockHttpRequest request = MockHttpRequest.delete("races/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteRaceGeneralException() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public void deleteRace(Race race) throws Exception {
                throw new Exception("exception");
            }
        };

        MockHttpRequest request = MockHttpRequest.delete("races/1");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);

        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void getRaces() throws Exception {
        new MockUp<RaceServiceImpl>() {
            @Mock
            public List<Race> getRaces() {
                List<Race> races = new ArrayList<>();
                Race race = new Race();
                race.setId(1L);
                race.setRaceOrder(1);
                race.setName("Race 1");
                races.add(race);
                return races;
            }
        };

        MockHttpRequest request = MockHttpRequest.get("races");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void checkGetRaceAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "races", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for GET:race");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for GET:race is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getRaces")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }

    @Test
    public void checkAddRaceAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "races", "POST");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for POST:race");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for POST:race is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("addRace")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }

    @Test
    public void checkDeleteRaceAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "races/{id}", "DELETE");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for DELETE:race");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for DELETE:race is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("removeRace")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }
}
