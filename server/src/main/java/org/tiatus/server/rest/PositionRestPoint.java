package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.tiatus.entity.Penalty;
import org.tiatus.entity.Position;
import org.tiatus.role.Role;
import org.tiatus.service.PositionService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/positions")
public class PositionRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRestPoint.class);

    @Autowired
    protected PositionService service;

    // /**
    //  * Get positions
    //  * @return response containing list of positions
    //  */
    // @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Position>  getPositions() {
        return service.getPositions();
    }

    // /**
    //  * Add position, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param position to add
    //  * @return 201 response with location containing uri of newly created position or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Position addPosition(@RequestBody Position position, HttpSession session) {
        LOG.debug("Adding position " + position);
        try {
            return service.addPosition(position, session.getId());

        } catch (Exception e) {
            logError(e);
        }

        return position;
    }

    // /**
    //  * Remove position, restricted to Admin users
    //  * @param id of position to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void removePosition(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing position with id " + id);
        try {
            Position position = service.getPositionForId(id);
            if (position != null) {
                service.removePosition(position, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update position, restricted to Admin users
    //  * @param id of position to update
    //  * @param position to update
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public Position updatePosition(@PathVariable("id") Long id, @RequestBody Position position, HttpSession session, HttpServletResponse response) {
        LOG.debug("Updating position with id " + id);
        try {
            Position existing = service.getPositionForId(id);
            if (existing == null) {
                LOG.warn("Failed to get position for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return position;
            }

            service.updatePosition(position, session.getId());

        } catch (Exception e) {
            logError(e);
        }

        return position;
    }
}
