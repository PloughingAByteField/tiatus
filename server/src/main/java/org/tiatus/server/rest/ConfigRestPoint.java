package org.tiatus.server.rest;

// import org.apache.commons.io.IOUtils;
// import org.jboss.resteasy.plugins.providers.multipart.InputPart;
// import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.service.ConfigService;

// import javax.inject.Inject;
// import javax.servlet.http.HttpServletRequest;
// import javax.ws.rs.*;
// import javax.ws.rs.core.Context;
// import javax.ws.rs.core.MultivaluedMap;
// import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Created by johnreynolds on 04/09/2016.
 */
// @Path("config")
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/config")
public class ConfigRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigRestPoint.class);

    @Autowired
    protected ConfigService service;

    @Autowired
    protected Environment environment;

    @GetMapping(path = "config", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody ByteArrayResource getFileViaByteArrayResource() throws IOException, URISyntaxException {
        Path path = Paths.get(environment.getProperty("tiatus.files") + "/tiatus/" + "config/config.json");
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return resource;
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "logo")
    public void uploadEventDetails(@ModelAttribute ConfigData input, HttpSession session) {
        // need to debug here to see how spring handles this
    //     try {
    //         Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    //         List<InputPart> inputParts = uploadForm.get("file");
    //         String logoFileName = "";
    //         for (InputPart inputPart : inputParts) {
    //             MultivaluedMap<String, String> header = inputPart.getHeaders();
    //             String fileName = getFileName(header);
    //             if (fileName != null) {
    //                 LOG.debug("Got uploaded file of name: " + fileName + " of " + request.getContentLength() + " bytes");
    //                 InputStream inputStream = inputPart.getBody(InputStream.class, null);
    //                 logoFileName = service.setEventLogo(inputStream, fileName);
    //                 break;
    //             }
    //         }

    //         return Response.ok(logoFileName).build();

    //     } catch (Exception e) {
    //         LOG.warn("File upload failed", e);
    //         return Response.status(Response.Status.BAD_REQUEST).build();
    //     }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "title")
    public void uploadEventTitle(String title, HttpServletResponse response) {
        try {
            String unquotedString = title.replaceAll("^\"|\"$", "");
            LOG.debug("Have title " + unquotedString);
            service.setEventTitle(unquotedString);

        } catch (Exception e) {
            LOG.warn("Event title upload failed", e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "footer")
    public void uploadEventFooter(String footer, HttpServletResponse response) {
        try {
            String unquotedString = footer.replaceAll("^\"|\"$", "");
            LOG.debug("Have footer " + unquotedString);
            service.setEventFooter(unquotedString);

        } catch (Exception e) {
            LOG.warn("Event footer upload failed", e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    // private String getFileName(MultivaluedMap<String, String> header) {
    //     String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

    //     for (String filename : contentDisposition) {
    //         if ((filename.trim().startsWith("filename"))) {

    //             String[] name = filename.split("=");

    //             String finalFileName = name[1].trim().replaceAll("\"", "");
    //             return finalFileName;
    //         }
    //     }
    //     return null;
    // }


}
