package org.tiatus.server.rest;

import mockit.Deencapsulation;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jboss.resteasy.spi.metadata.ResourceMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tiatus.entity.Race;
import org.tiatus.service.RaceServiceImpl;
import org.tiatus.service.ServiceException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by johnreynolds on 09/07/2016.
 */
public class RaceRestPointTest {

    private Dispatcher dispatcher = null;
    private POJOResourceFactory endPoint = null;
    private List<EndPointDetail> endPointDetails;

    @Before
    public void setup() throws Exception {
        new MockUp<RaceRestPoint>() {
            @Mock
            void $init(Invocation invocation) { // need to supply the CDI injected object which we are mocking
                RaceRestPoint restPoint = invocation.getInvokedInstance();
                RaceServiceImpl service = new RaceServiceImpl(null);
                Deencapsulation.setField(restPoint, "service", service);
            }
        };

        dispatcher = MockDispatcherFactory.createDispatcher();
        endPoint = new POJOResourceFactory(RaceRestPoint.class);
        dispatcher.getRegistry().addResourceFactory(endPoint);
        endPointDetails = fillEndPointDetails(endPoint);
    }

    private List<EndPointDetail> fillEndPointDetails(POJOResourceFactory endPoint) throws Exception {
        List<EndPointDetail> details = new ArrayList<>();

        ResourceClass resource = ResourceBuilder.rootResourceFromAnnotations(endPoint.getScannableClass());
        ResourceMethod[] methods = resource.getResourceMethods();
        for (ResourceMethod method : methods) {
            EndPointDetail detail = new EndPointDetail();
            detail.setPath(method.getFullpath());

            if (method.getHttpMethods() != null) {
                for (String httpMethod : method.getHttpMethods()) {
                    detail.addHttpMethod(httpMethod);
                }
            }

            if (method.getConsumes() != null) {
                for (MediaType type : method.getConsumes()) {
                    detail.addConsumes(type.toString());
                }
            }

            if (method.getProduces() != null) {
                for (MediaType type : method.getProduces()) {
                    detail.addProduces(type.toString());
                }
            }

            Method am = method.getAnnotatedMethod();
            if (am != null) {
                detail.setMethodName(am.getName());

                Annotation[] declared = am.getDeclaredAnnotations();
                if (declared != null) {
                    for (Annotation annotation : declared) {

                        if (annotation.annotationType().equals(RolesAllowed.class)) {
                            RolesAllowed allowed = (RolesAllowed) annotation;
                            String[] allowedValues = allowed.value();
                            if (allowedValues != null) {
                                for (String value : allowedValues) {
                                    detail.addRoleAllowed(value);
                                }
                            }

                        } else if (annotation.annotationType().equals(DenyAll.class)) {
                            detail.setDenyAll(true);

                        } else if (annotation.annotationType().equals(PermitAll.class)) {
                            detail.setAllowAll(true);
                        }
                    }
                }
            }

            details.add(detail);
        }

        return details;
    }

    private EndPointDetail getEndPointDetail(List<EndPointDetail> details, String path, String httpMethod) {
        for (EndPointDetail endPointDetail : endPointDetails) {
            if (endPointDetail.getPath().equals(path) && endPointDetail.getHttpMethod().equals(httpMethod)) {
                return endPointDetail;
            }
        }

        return null;
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
        MockHttpRequest request = MockHttpRequest.get("race");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void checkAddRaceAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "race", "POST");
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

        if (endPointDetail.isAllowAll() == null || endPointDetail.isAllowAll() == false) {
            System.out.println("End point is not allowed all");
            throw new Exception();
        }
    }

    @Test
    public void checkGetOtherRaceAnnotations() throws Exception {
        EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "race/other", "GET");
        if (endPointDetail == null) {
            System.out.println("Failed to find end point for POST:race");
            throw new Exception();
        }

        if (!EndPointDetail.isValid(endPointDetail)) {
            System.out.println("End point for POST:race is not valid");
            throw new Exception();
        }

        if (!endPointDetail.getMethodName().equals("getRacesOther")) {
            System.out.println("End point method name has changed");
            throw new Exception();
        }

        if (endPointDetail.getRolesAllowed().isEmpty()) {
            System.out.println("End point does not have roles allowed");
            throw new Exception();
        }

        List<String> expectedRoles = new ArrayList<>(Arrays.asList("role2", "role1"));
        if (! endPointDetail.getRolesAllowed().containsAll(expectedRoles)) {
            System.out.println("End point does not have expected roles");
            throw new Exception();
        }
    }


}
