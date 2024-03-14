package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tiatus.entity.User;
import org.tiatus.service.ConfigService;
import org.tiatus.service.UserService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

// import javax.annotation.security.PermitAll;

import java.io.InputStream;

/**
 * Created by johnreynolds on 04/09/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/setup")
public class SetupRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(SetupRestPoint.class);

    @Autowired
    protected ConfigService configService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ServletContext context;
    
    // /**
    //  * Add an admin user via setup page
    //  * @param uriInfo location details
    //  * @param user user to add
    //  * @return response contain success or failure code
    //  */
    // @PermitAll
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "user")
    public User addUser(@RequestBody User user, HttpSession session) {
        LOG.warn("1 Adding admin user " + user.getUserName());
        if (!checkIfSetupHasAlreadyRun()) {
            LOG.debug("2 Adding admin user " + user.getUserName());
            try {
                User adminUser = userService.addAdminUser(user, session.getId());
                // create default entries in config file
                configService.setEventTitle("Tiatus Timing System");
                InputStream logoStream = getDefaultLogoFile(context);
                if (logoStream != null) {
                    configService.setEventLogo(logoStream, "tiatus.svg");
                } else {
                    LOG.warn("Failed to find image");
                }
                return adminUser;

            } catch (Exception e) {
                logError(e);
            }
        }

        return null;
    }

    private InputStream getDefaultLogoFile(ServletContext context) {
        return context.getResourceAsStream("/assets/img/stopwatch.svg");
    }

    private boolean checkIfSetupHasAlreadyRun() {
        if (userService.hasAdminUser()) {
            LOG.warn("Already have run setup");
            return true;
        }

        return false;
    }
}
