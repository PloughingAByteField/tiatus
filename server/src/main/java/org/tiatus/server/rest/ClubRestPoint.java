package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.MediaType;
import org.tiatus.entity.Club;
import org.tiatus.service.ClubService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// import org.tiatus.role.Role;
import org.springframework.web.bind.annotation.RestController;

// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@RestController
@RequestMapping("clubs")
// @SuppressWarnings("squid:S1166")
public class ClubRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ClubRestPoint.class);

    @Autowired
    protected ClubService service;

    // /**
    //  * Get clubs
    //  * @return response containing list of clubs
    //  */
    // @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Club> getClubs() {
        LOG.info("Get clubs");
        List<Club> clubs = service.getClubs();
        return clubs;
    }

    // /**
    //  * Add club, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param club to add
    //  * @return 201 response with location containing uri of newly created club or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Club addClub(HttpSession session, Club club) {
        LOG.debug("Adding club " + club);
        try {
            return service.addClub(club, session.getId());

        } catch (Exception e) {
            logError(e);
        }
        return club;
    }

    // /**
    //  * Remove club, restricted to Admin users
    //  * @param id of club to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public void removeClub(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing club with id " + id);
        try {
            Club club = service.getClubForId(id);
            if (club != null) {
                service.deleteClub(club, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update club, restricted to Admin users
    //  * @param id of club to update
    //  * @param club to update
    //  * @return 200 response or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public Club updateClub(@PathVariable("id") Long id, HttpSession session, Club club, HttpServletResponse response) {
        LOG.debug("updating club of id " + id);
        try {
            Club existing = service.getClubForId(id);
            if (existing == null) {
                LOG.warn("Failed to get club for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }

            return service.updateClub(club, session.getId());

        } catch (Exception e) {
           logError(e);
        }
        return club;
    }
}
