package org.tiatus.server.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Created by johnreynolds on 04/09/2016.
 */
@Path("setup")
@SuppressWarnings("squid:S1166")
public class SetupRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(SetupRestPoint.class);

    private UserService service;

    /**
     * Add an admin user via setup page
     * @param uriInfo location details
     * @param user user to add
     * @return response contain success or failure code
     */
    @PermitAll
    @POST
    @Path("user")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        LOG.debug("Adding user " + user);
        try {
            service.addUser(user);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ user.getId())).entity(user).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @POST
    @Path("logo")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response uploadEventDetails(MultipartFormDataInput input, @Context HttpServletRequest request) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");
            for (InputPart inputPart : inputParts) {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
                if (fileName != null) {
                    LOG.debug("Got uploaded file of name: " + fileName + " of " + request.getContentLength() + " bytes");

                    //convert the uploaded file to inputstream
                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    String logoFileName = "/tiatus/" + fileName;

                    File logoFile = new File(System.getProperty("jboss.home.dir") + logoFileName);
                    logoFile.getParentFile().mkdirs();
                    try (FileOutputStream fop = new FileOutputStream(logoFile)) {
                        IOUtils.copy(inputStream, fop);
                        fop.close();

                    } catch (IOException e) {
                        LOG.warn("Got general exception ", e);
                        throw new InternalServerErrorException();
                    }
                    updateConfig("logo", logoFileName);
                    break;
                }
            }

            return Response.ok().build();

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
            LOG.debug("Have title " + title);
            updateConfig("title", title);

            return Response.ok().build();

        } catch (Exception e) {
            LOG.warn("File upload failed", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(UserService service) {
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

    private synchronized void updateConfig(String key, String data) throws IOException {
        File configFile = new File(System.getProperty("jboss.home.dir") + "/tiatus/" + "config/config.json");
        configFile.getParentFile().mkdirs();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        if (!configFile.exists()) {
            LOG.debug("Creating file " + configFile.getAbsolutePath());
            root = mapper.createObjectNode();
        } else {
            root = mapper.readTree(configFile);
        }

        ((ObjectNode) root).put(key, data);
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        mapper.writeValue(configFile, root);
    }
}
