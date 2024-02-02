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
import org.tiatus.entity.Event;
import org.tiatus.entity.EventPosition;
import org.tiatus.entity.Position;
import org.tiatus.entity.RaceEvent;
import org.tiatus.role.Role;
import org.tiatus.service.EventService;
import org.tiatus.service.PositionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 29/10/2016.
 */
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/events")
public class EventRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(EventRestPoint.class);

    @Autowired
    protected EventService service;

    @Autowired
    protected PositionService positionService;

    // /**
    //  * Add event, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param event to add
    //  * @return 201 response with location containing uri of newly created event or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> createEvent(@RequestBody Event event, HttpSession session, HttpServletRequest request) {
        LOG.debug("Adding event name " + event.getName());
        try {
            for (EventPosition eventPosition: event.getPositions()) {
                Position position = positionService.getPositionForId(eventPosition.getPositionId());
                eventPosition.setPosition(position);
            }
            
            Event newEvent = service.addEvent(event, session.getId());
            URI location = URI.create(request.getRequestURI() + "/"+ newEvent.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("location", location.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);


        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Update event, restricted to Admin users
    //  * @param id of event to update
    //  * @param event to update
    //  * @return 201 response with location containing uri of newly created event or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> updateEvent(@PathVariable("id") Long id, @RequestBody Event event, HttpSession session, HttpServletResponse response) {
        LOG.debug("Updating event of id " + event.getId());
        Event existing = service.getEventForId(id);
        if (existing == null) {
            LOG.warn("Failed to get event for supplied id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            for (EventPosition eventPosition: event.getPositions()) {
                Position position = positionService.getPositionForId(eventPosition.getPositionId());
                eventPosition.setPosition(position);
            }

            service.updateEvent(event, session.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Remove event, restricted to Admin users
    //  * @param id of event to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void removeEvent(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Got event id " + id);
        try {
            Event event = service.getEventForId(id);
            if (event != null) {
                service.deleteEvent(event, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Remove event, restricted to Admin users
    //  * @param id of event to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "unassigned/{id}")
    public void removeUnassignedEvent(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Got event id " + id);
        removeEvent(id, session);
    }

    // /**
    //  * Get events
    //  * @return response containing list of events
    //  */
    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Event> getEvents() {
        return service.getEvents();
    }

    // /**
    //  * Get events assigned to races
    //  * @return response containing list of events assigned to races
    //  */
    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "assigned")
    public List<RaceEvent> getAssignedEvents() {
        return service.getAssignedEvents();
    }

    // /**
    //  * Remove assigned event, restricted to Admin users
    //  * @param id of assigned event to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "assigned/{id}")
    public void removeAssignedEvent(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Got raceEvent id " + id);
        try {
            RaceEvent raceEvent = service.getRaceEventForId(id);
            if (raceEvent != null) {
                service.deleteRaceEvent(raceEvent, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Add assigned event, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param raceEvent to add
    //  * @return 201 response with location containing uri of newly created raceEvent or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "assigned")
    public RaceEvent addAssignedEvent(@RequestBody RaceEvent raceEvent, HttpSession session) {
        try {
            return service.addRaceEvent(raceEvent, session.getId());

        } catch (Exception e) {
            logError(e);
        }

        return raceEvent;
    }

    // /**
    //  * Update assigned events, restricted to Admin users
    //  * @param raceEvents list containing race events to update
    //  * @return 200 response or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "assigned")
    public ResponseEntity<Void> updateAssignedEvents(@RequestBody List<RaceEvent> raceEvents, HttpSession session, HttpServletResponse response) {
        LOG.debug("updating assigned events");
        try {
            for (RaceEvent raceEventToUpdate : raceEvents) {
                RaceEvent existing = service.getRaceEventForId(raceEventToUpdate.getId());
                if (existing == null) {
                    LOG.warn("Failed to get race event for supplied id");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

            service.updateRaceEvents(raceEvents, session.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            logError(e);
        }
        
        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Get events not assigned to any race
    //  * @return response containing list of events not assigned to any race
    //  */
    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "unassigned")
    public List<Event>  getUnassignedEvents() {
        return service.getUnassignedEvents();
    }

}
