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
import org.tiatus.entity.Disqualification;
import org.tiatus.entity.Entry;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.EntryService;
import org.tiatus.service.RaceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("entries")
public class EntryRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(EntryRestPoint.class);

    @Autowired
    protected EntryService service;

    @Autowired
    protected RaceService raceService;

    /**
     * Get entries
     * @return response containing list of entries
     */
    // @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Entry> getEntries() {
        return service.getEntries();
    }

    // @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "race/{raceId}")
    public List<Entry> getEntriesForRace(@PathVariable("raceId") Long raceId, HttpSession session, HttpServletResponse response) {
        Race race = raceService.getRaceForId(raceId);
        if (race == null) {
            LOG.warn("Failed to get race for supplied id");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return new ArrayList<>();
        }

        return service.getEntriesForRace(race);
    }

    // /**
    //  * Add entry, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param entry to add
    //  * @return 201 response with location containing uri of newly created entry or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public URI addEntry(@RequestBody Entry entry, HttpServletRequest request) {
        LOG.debug("Adding entry " + entry);
        try {
            Entry saved = service.addEntry(entry, request.getSession().getId());
            return URI.create(request.getServletContext().getContextPath() + "/"+ saved.getId());

        } catch (Exception e) {
            logError(e);
        }

        return null;
    }

    // /**
    //  * Remove entry, restricted to Admin users
    //  * @param id of entry to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void removeEntry(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing entry with id " + id);
        try {
            Entry entry = service.getEntryForId(id);
            if (entry != null) {
                service.deleteEntry(entry, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update entry, restricted to Admin users
    //  * @param entry to update
    //  * @return 404 if entity does not exist else return 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public Entry updateEntry(@PathVariable("id") Long id, @RequestBody Entry entry, HttpSession session, HttpServletResponse response) {
        LOG.debug("Updating entry " + id);
        try {
            Entry existing = service.getEntryForId(id);
            if (existing == null) {
                LOG.warn("Failed to get entry for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return entry;
            }

            return service.updateEntry(entry, session.getId());

        } catch (Exception e) {
            logError(e);
        }

        return entry;
    }

    // /**
    //  * Update list of entries, restricted to Admin users
    //  * @param entries to update
    //  * @return 201 response with location containing uri of newly created entry or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "updates")
    public void updateEntries(@RequestBody List<Entry> entries, HttpSession session, HttpServletResponse response) {
        LOG.debug("Updating " + entries.size() + " entries");
        try {
            if (entries.size() == 0) {
                LOG.warn("Got empty entries updates");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }
            Set<Race> races = new HashSet<>();
            for (Entry entry: entries) {
                Entry existing = service.getEntryForId(entry.getId());
                if (existing == null) {
                    LOG.warn("Failed to get entry for supplied id");
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                    return;
                }
                races.add(entry.getRace());
            }

            service.updateEntries(entries, session.getId());
            response.setStatus(HttpStatus.NO_CONTENT.value());

        } catch (Exception e) {
            logError(e);
        }
    }

    // @RolesAllowed({Role.ADJUDICATOR})
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "swapEntries/{fromId}/{toId}")
    public void swapEntries(@PathVariable("fromId") Long fromId, @PathVariable("toId") Long toId, HttpSession session, HttpServletResponse response) {
        try {
            Entry from = service.getEntryForId(fromId);
            Entry to = service.getEntryForId(toId);
            if (from == null || to == null) {
                LOG.warn("Failed to get entry for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            service.swapEntryNumbers(from, to, session.getId());

        } catch (Exception e) {
            logError(e);
        }
    }

}
