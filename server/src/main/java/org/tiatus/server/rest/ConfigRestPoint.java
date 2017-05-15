package org.tiatus.server.rest;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.service.ConfigService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by johnreynolds on 04/09/2016.
 */
@Path("config")
@SuppressWarnings("squid:S1166")
public class ConfigRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigRestPoint.class);

    private ConfigService service;

    @POST
    @Path("logo")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response uploadEventDetails(MultipartFormDataInput input, @Context HttpServletRequest request) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");
            String logoFileName = "";
            for (InputPart inputPart : inputParts) {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
                if (fileName != null) {
                    LOG.debug("Got uploaded file of name: " + fileName + " of " + request.getContentLength() + " bytes");
                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    logoFileName = service.setEventLogo(inputStream, fileName);
                    break;
                }
            }

            return Response.ok(logoFileName).build();

        } catch (Exception e) {
            LOG.warn("File upload failed", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("title")
    @Consumes("application/json")
    @Produces("application/json")
    public Response uploadEventTitle(String title) {
        try {
            String unquotedString = title.replaceAll("^\"|\"$", "");
            LOG.debug("Have title " + unquotedString);
            service.setEventTitle(unquotedString);
            return Response.ok().build();

        } catch (Exception e) {
            LOG.warn("Event title upload failed", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("footer")
    @Consumes("application/json")
    @Produces("application/json")
    public Response uploadEventFooter(String footer) {
        try {
            String unquotedString = footer.replaceAll("^\"|\"$", "");
            LOG.debug("Have footer " + unquotedString);
            service.setEventFooter(unquotedString);
            return Response.ok().build();

        } catch (Exception e) {
            LOG.warn("Event footer upload failed", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(ConfigService service) {
        this.service = service;
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return null;
    }


}
