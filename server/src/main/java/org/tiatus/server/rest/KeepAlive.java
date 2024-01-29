package org.tiatus.server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import javax.annotation.security.PermitAll;

/**
 * Created by johnreynolds on 21/04/2017.
 */
// @Path("keepalive")
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("keepalive")
public class KeepAlive {

    private static final Logger LOG = LoggerFactory.getLogger(KeepAlive.class);

    // @PermitAll
    @GetMapping()
    public void keepAlive(HttpServletRequest request, HttpServletResponse response) {
        if (request == null || request.getSession(false) == null || request.getSession(false).getAttribute("userId") == null) {
            LOG.warn("User is not logged in");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        response.setStatus(HttpStatus.OK.value());
    }
}
