package org.tiatus.server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by johnreynolds on 21/04/2017.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/keepalive")
public class KeepAlive {

    private static final Logger LOG = LoggerFactory.getLogger(KeepAlive.class);

    @GetMapping()
    public void keepAlive(HttpServletResponse response, HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_CONTEXT") == null) {
            LOG.warn("User is not logged in");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        response.setStatus(HttpStatus.OK.value());
    }
}
