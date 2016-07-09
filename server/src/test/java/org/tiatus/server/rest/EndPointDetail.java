package org.tiatus.server.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 09/07/2016.
 */
public class EndPointDetail {
    private List<String> httpMethods = new ArrayList<>();
    private String path;
    private String methodName;
    private List<String> consumes = new ArrayList<>();
    private List<String> produces = new ArrayList<>();
    private List<String> rolesAllowed = new ArrayList<>();
    private Boolean allowAll = null;
    private Boolean denyAll = null;

    public String getHttpMethod() {
        if (httpMethods.size() > 0) {
            return httpMethods.get(0);
        }

        return null;
    }

    public void addHttpMethod(String httpMethod) {
        this.httpMethods.add(httpMethod);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void addConsumes(String consumes) {
        this.consumes.add(consumes);
    }

    public List<String> getProduces() {
        return produces;
    }

    public void addProduces(String produces) {
        this.produces.add(produces);
    }

    public List<String> getRolesAllowed() {
        return rolesAllowed;
    }

    public void addRoleAllowed(String roleAllowed) {
        this.rolesAllowed.add(roleAllowed);
    }

    public Boolean isAllowAll() {
        return allowAll;
    }

    public void setAllowAll(boolean allowAll) {
        this.allowAll = allowAll;
    }

    public Boolean isDenyAll() {
        return denyAll;
    }

    public void setDenyAll(boolean denyAll) {
        this.denyAll = denyAll;
    }

    public static boolean isValid(EndPointDetail detail) {
        if (detail.isDenyAll() != null) {
            if (detail.isAllowAll() != null || detail.getRolesAllowed().size() > 0) {
                return false;
            }
        }

        if (detail.isAllowAll() != null) {
            if (detail.getRolesAllowed().size() > 0) {
                return false;
            }
        }

        if (detail.httpMethods.size() > 1) {
            return false;
        }

        return true;
    }
}
