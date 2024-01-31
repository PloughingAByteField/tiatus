package org.tiatus.server.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by johnreynolds on 27/08/2016.
 */
@RestController
@RequestMapping("/rest/logout")
public class LogoutRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutRestPoint.class);

    /**
     * Logout and redirect a user back to login page
     * @param uriInfo location infor
     * @param httpServletRequest servlet request
     * @return user to login page
     */
    // @PermitAll
    @GetMapping()
    public void logout(HttpSession session, HttpServletResponse response) {
        try {
            
            session.invalidate();

            
            LOG.debug("Redirecting to /login");
            response.sendRedirect("/login");

        } catch (Exception e) {
            logError(e);
        }
    }
}
