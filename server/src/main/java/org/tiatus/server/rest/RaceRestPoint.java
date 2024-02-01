package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.RaceService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/races")
public class RaceRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    @Autowired
    protected RaceService service;

    // /**
    //  * Get races
    //  * @return response containing list of races
    //  */
    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Race> getRaces() {
        return service.getRaces();
    }

    // /**
    //  * Add race, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param race to add
    //  * @return 201 response with location containing uri of newly created race or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> addRace(@RequestBody Race race, HttpSession session, HttpServletRequest request) {
        LOG.debug("Adding race " + race);
        try {
            Race newRace = service.addRace(race, session.getId());
            URI location = URI.create(request.getRequestURI() + "/"+ newRace.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("location", location.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Remove race, restricted to Admin users
    //  * @param id of race to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void removeRace(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing race with id " + id);
        try {
            Race race = service.getRaceForId(id);
            if (race != null) {
                service.deleteRace(race, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update race, restricted to Adjudicator and Admin users
    //  * @param id of race to update
    //  * @param race of race to update
    //  * @return response with 204
    //  */
    @RolesAllowed({Role.ADMIN, Role.ADJUDICATOR})
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public ResponseEntity<Void> updateRace(@PathVariable("id") Long id, @RequestBody Race race, HttpSession session) {
        LOG.debug("Updating race with id " + id + " race " + race.getId());
        try {
            Race existing = service.getRaceForId(id);
            if (existing == null) {
                LOG.warn("Failed to get race for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            service.updateRace(race, session.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }
}
