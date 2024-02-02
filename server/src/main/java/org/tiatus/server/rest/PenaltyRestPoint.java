package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Penalty;
import org.tiatus.role.Role;
import org.tiatus.service.PenaltyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @Path("penalties")
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/penalties")
public class PenaltyRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyRestPoint.class);

    @Autowired
    protected PenaltyService service;

    // /**
    //  * Get penalties
    //  * @return response containing list of penalties
    //  */
    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Penalty> getPenalties() {
        return service.getPenalties();
    }

    // /**
    //  * Add penalty, restricted to Adjudicator users
    //  * @param uriInfo location details
    //  * @param penalty to add
    //  * @return 201 response with location containing uri of newly created penalty or an error code
    //  */
    @RolesAllowed(Role.ADJUDICATOR)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> addPenalty(@RequestBody Penalty penalty, HttpSession session, HttpServletRequest request) {
        LOG.debug("Adding penalty " + penalty);
        try {
            Penalty newPenalty = service.addPenalty(penalty, session.getId());
            URI location = URI.create(request.getRequestURI() + "/"+ newPenalty.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("location", location.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Remove penalty, restricted to Adjudicator users
    //  * @param id of penalty to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADJUDICATOR)
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> removePenalty(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing penalty with id " + id);
        try {
            Penalty penalty = service.getPenaltyForId(id);
            if (penalty != null) {
                service.deletePenalty(penalty, session.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            } else {
                LOG.warn("Failed to get event for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Update penalty, restricted to Adjudicator users
    //  * @param id of penalty to update
    //  * @param penalty to update
    //  * @return 200 response or an error code
    //  */
    @RolesAllowed(Role.ADJUDICATOR)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public ResponseEntity<Void> updatePenalty(@PathVariable("id") Long id, @RequestBody Penalty penalty, HttpSession session, HttpServletResponse response) {
        LOG.debug("updating penalty");
        try {
            Penalty existing = service.getPenaltyForId(id);
            if (existing == null) {
                LOG.warn("Failed to get penalty for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            service.updatePenalty(penalty, session.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

}
