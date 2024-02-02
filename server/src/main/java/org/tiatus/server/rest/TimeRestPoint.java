package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tiatus.entity.Entry;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 08/02/2017.
 */
@RestController
@RequestMapping("/rest/time")
public class TimeRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(TimeRestPoint.class);

    @Autowired
    protected TimesService service;

    @Autowired
    protected RaceService raceService;

    @Autowired
    protected EntryService entryService;

    @Autowired
    protected PositionService positionService;

    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "position/{positionId}/race/{raceId}")
    public List<EntryPositionTime> getListOfEntriesAndTimesForPositionInRace(@PathVariable("positionId") Long positionId, @PathVariable("raceId") Long raceId, HttpServletResponse response) {
        try {
            Race race = raceService.getRaceForId(raceId);
            Position position = positionService.getPositionForId(positionId);
            if (race == null || position == null) {
                LOG.warn("Failed to get race or position for supplied ids");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new ArrayList<>();
            }

            return service.getPositionTimesForPositionInRace(race, position);

        } catch (Exception e) {
            logError(e);
        }

        return new ArrayList<>();
    }

    @RolesAllowed(Role.TIMING)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "position/{positionId}/entry/{entryId}")
    public EntryPositionTime saveTime(@PathVariable("positionId") Long positionId, @PathVariable("entryId") Long entryId, @RequestBody EntryPositionTime entryPositionTime, HttpSession session, HttpServletResponse response) {
        try {
            Position position = positionService.getPositionForId(positionId);
            entryPositionTime.setPosition(position);
            Entry entry = entryService.getEntryForId(entryId);
            entryPositionTime.setEntry(entry);
            if (entry == null || position == null) {
                LOG.warn("Failed to get entry or position for supplied ids");
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }

            LOG.debug("have entry " + entryPositionTime.getEntry().getId());
            LOG.debug("Have time " + entryPositionTime.getTime());
            return service.createTime(entryPositionTime, session.getId());

        } catch (Exception e) {
            logError(e);
        }

        return entryPositionTime;
    }

    @RolesAllowed(Role.TIMING)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "position/{positionId}/entry/{entryId}")
    public EntryPositionTime updateTime(@PathVariable("positionId") Long positionId, @PathVariable("entryId") Long entryId, @RequestBody EntryPositionTime entryPositionTime, HttpSession session) {
        try {
            Position position = positionService.getPositionForId(positionId);
            entryPositionTime.setPosition(position);
            Entry entry = entryService.getEntryForId(entryId);
            entryPositionTime.setEntry(entry);
            return service.updateTime(entryPositionTime, session.getId());

        } catch (Exception e) {
            logError(e);
        }

        return entryPositionTime;
    }

    @PermitAll
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "race/{raceId}")
    public List<EntryPositionTime> getListOfTimesForRace(@PathVariable("raceId") Long raceId, HttpServletResponse response) {
        try {
            Race race = raceService.getRaceForId(raceId);
            if (race == null) {
                LOG.warn("Failed to get race for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new ArrayList<>();
            }

            return service.getTimesForRace(race);

        } catch (Exception e) {
            logError(e);
        }

        return new ArrayList<>();
    }

    @RolesAllowed(Role.ADJUDICATOR)
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "race/{raceId}/full")
    public List<EntryPositionTime> getListOfFullTimesForRace(@PathVariable("raceId") Long raceId, HttpServletResponse response) {
        try {
            Race race = raceService.getRaceForId(raceId);
            if (race == null) {
                LOG.warn("Failed to get race for supplied id");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new ArrayList<>();
            }

            return service.getAllTimesForRace(race);

        } catch (Exception e) {
            logError(e);
        }

        return new ArrayList<>();
    }

}
