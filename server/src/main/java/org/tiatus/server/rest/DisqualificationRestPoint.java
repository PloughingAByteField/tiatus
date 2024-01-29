package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Disqualification;
import org.tiatus.role.Role;
import org.tiatus.service.DisqualificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;
// import javax.inject.Inject;
// import javax.servlet.http.HttpServletRequest;
// import javax.ws.rs.*;
// import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("disqualifications")
public class DisqualificationRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationRestPoint.class);

    @Autowired
    protected DisqualificationService service;

    /**
     * Get disqualifications
     * @return response containing list of disqualifications
     */
    // @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Disqualification>  getDisqualifications() {
        return service.getDisqualifications();
    }

    // /**
    //  * Add disqualification, restricted to Adjudicator users
    //  * @param uriInfo location details
    //  * @param disqualification to add
    //  * @return 201 response with location containing uri of newly created disqualification or an error code
    //  */
    // @RolesAllowed({Role.ADJUDICATOR})
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public URI addDisqualification(@RequestBody Disqualification disqualification, HttpServletRequest request) {
        LOG.debug("Adding disqualification " + disqualification);
        try {
            Disqualification saved = service.addDisqualification(disqualification, request.getSession().getId());
            return URI.create(request.getServletContext().getContextPath() + "/"+ saved.getId());

        } catch (Exception e) {
            logError(e);
        }

        return null;
    }

    // /**
    //  * Remove disqualification, restricted to Adjudicator users
    //  * @param id of disqualification to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADJUDICATOR})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void removeDisqualification(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing disqualification with id " + id);
        try {
            Disqualification disqualification = service.getDisqualificationForId(id);
            if (disqualification != null) {
                service.deleteDisqualification(disqualification, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update disqualification, restricted to Adjudicator users
    //  * @param id of disqualification to update
    //  * @param disqualification to update
    //  * @return 200 response or an error code
    //  */
    // @RolesAllowed({Role.ADJUDICATOR})
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public Disqualification updateDisqualification(@PathVariable("id") Long id, @RequestBody Disqualification disqualification, HttpSession session, HttpServletResponse response) {
        LOG.debug("updating disqualification");
        try {
            Disqualification existing = service.getDisqualificationForId(id);
            if (existing == null) {
                LOG.warn("Failed to get disqualification for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return disqualification;
            }

            return service.updateDisqualification(disqualification, session.getId());

        } catch (Exception e) {
            logError(e);
        }
        return disqualification;
    }

}
