package org.tiatus.server.rest;

// import org.jboss.resteasy.core.Dispatcher;
// import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
// import org.jboss.resteasy.spi.metadata.ResourceBuilder;
// import org.jboss.resteasy.spi.metadata.ResourceClass;
// import org.jboss.resteasy.spi.metadata.ResourceMethod;

// import javax.annotation.security.DenyAll;
// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;
// import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 30/10/2016.
 */
public abstract class RestTestBase {

    // protected Dispatcher dispatcher = null;
    // protected POJOResourceFactory endPoint = null;
    // protected List<EndPointDetail> endPointDetails;

    // protected List<EndPointDetail> fillEndPointDetails(POJOResourceFactory endPoint) throws Exception {
    //     List<EndPointDetail> details = new ArrayList<>();

    //     ResourceClass resource = ResourceBuilder.rootResourceFromAnnotations(endPoint.getScannableClass());
    //     ResourceMethod[] methods = resource.getResourceMethods();
    //     for (ResourceMethod method : methods) {
    //         EndPointDetail detail = new EndPointDetail();
    //         detail.setPath(method.getFullpath());

    //         if (method.getHttpMethods() != null) {
    //             for (String httpMethod : method.getHttpMethods()) {
    //                 detail.addHttpMethod(httpMethod);
    //             }
    //         }

    //         if (method.getConsumes() != null) {
    //             for (MediaType type : method.getConsumes()) {
    //                 detail.addConsumes(type.toString());
    //             }
    //         }

    //         if (method.getProduces() != null) {
    //             for (MediaType type : method.getProduces()) {
    //                 detail.addProduces(type.toString());
    //             }
    //         }

    //         Method am = method.getAnnotatedMethod();
    //         if (am != null) {
    //             detail.setMethodName(am.getName());

    //             Annotation[] declared = am.getDeclaredAnnotations();
    //             if (declared != null) {
    //                 for (Annotation annotation : declared) {

    //                     if (annotation.annotationType().equals(RolesAllowed.class)) {
    //                         RolesAllowed allowed = (RolesAllowed) annotation;
    //                         String[] allowedValues = allowed.value();
    //                         for (String value : allowedValues) {
    //                             detail.addRoleAllowed(value);
    //                         }

    //                     } else if (annotation.annotationType().equals(DenyAll.class)) {
    //                         detail.setDenyAll(true);

    //                     } else if (annotation.annotationType().equals(PermitAll.class)) {
    //                         detail.setAllowAll(true);
    //                     }
    //                 }
    //             }
    //         }

    //         details.add(detail);
    //     }

    //     return details;
    // }

    // protected EndPointDetail getEndPointDetail(List<EndPointDetail> details, String path, String httpMethod) {
    //     for (EndPointDetail endPointDetail : endPointDetails) {
    //         if (endPointDetail.getPath().equals(path) && endPointDetail.getHttpMethod().equals(httpMethod)) {
    //             return endPointDetail;
    //         }
    //     }

    //     return null;
    // }
}
