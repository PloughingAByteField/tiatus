package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.role.Role;
import org.tiatus.service.ConfigService;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Created by johnreynolds on 04/09/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/config")
public class ConfigRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigRestPoint.class);

    @Autowired
    protected ConfigService service;

    @Autowired
    protected Environment environment;

    @PermitAll
    @GetMapping(path = "config", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody ByteArrayResource getConfigFile() throws IOException, URISyntaxException {
        Path path = Paths.get(environment.getProperty("tiatus.files") + "/tiatus/" + "config/config.json");
        if (Files.exists(path)) {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return resource;
        }

        String defaultConfig = "{\"title\":\"Tiatus Timing System\",\"footer\":\"Tiatus\",\"logo\":\"/results/assets/img/stopwatch.svg\"}";
        ByteArrayResource resource = new ByteArrayResource(defaultConfig.getBytes());
        return resource;
    }

    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, path = "logo")
    public void uploadEventLogo(
			@RequestParam("file") MultipartFile file, HttpSession session, HttpResponse response) {
        try {
            String originalName = file.getOriginalFilename();
            service.setEventLogo(file.getInputStream(), originalName);
            response.setStatus(HttpResponseStatus.OK);

        } catch (Exception e) {
            LOG.warn("File upload failed", e);
            response.setStatus(HttpResponseStatus.BAD_REQUEST);
        }
    }

    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "title")
    public void uploadEventTitle(@RequestBody String title, HttpServletResponse response) {
        try {
            String unquotedString = title.replaceAll("^\"|\"$", "");
            LOG.debug("Have title " + unquotedString);
            service.setEventTitle(unquotedString);

        } catch (Exception e) {
            LOG.warn("Event title upload failed", e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "footer")
    public void uploadEventFooter(@RequestBody String footer, HttpServletResponse response) {
        try {
            String unquotedString = footer.replaceAll("^\"|\"$", "");
            LOG.debug("Have footer " + unquotedString);
            service.setEventFooter(unquotedString);

        } catch (Exception e) {
            LOG.warn("Event footer upload failed", e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}
