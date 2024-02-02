package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Position;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;
import org.tiatus.role.Role;
import org.tiatus.service.PositionService;
import org.tiatus.service.RacePositionTemplateService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @Path("racePositionTemplates")
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/racePositionTemplates")
public class RacePositionTemplateRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateRestPoint.class);

    @Autowired
    protected RacePositionTemplateService service;

    @Autowired
    protected PositionService positionService;

    // /**
    //  * Get templates restricted to Admin users
    //  * @return response containing list of templates
    //  */
    @RolesAllowed(Role.ADMIN)
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<RacePositionTemplate> getTemplates() {
        return service.getRacePositionTemplates();
    }

    // /**
    //  * Add template, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param template to add
    //  * @return 201 response with location containing uri of newly created template or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void>  addTemplate(@RequestBody RacePositionTemplate template, HttpSession session, HttpServletRequest request) {
        LOG.debug("Adding template " + template.getName());
        try {
            RacePositionTemplate newRacePositionTemplate = service.addRacePositionTemplate(template, session.getId());
            URI location = URI.create(request.getRequestURI() + "/"+ newRacePositionTemplate.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("location", location.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Remove template, restricted to Admin users
    //  * @param id of template to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void deleteTemplate(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing template with id " + id);
        try {
            RacePositionTemplate template = service.getTemplateForId(id);
            if (template != null) {
                service.deleteRacePositionTemplate(template, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update template, restricted to  Admin users
    //  * @param id of template to update
    //  * @param template of template to update
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public ResponseEntity<Void> updateTemplate(@PathVariable("id") Long id, @RequestBody RacePositionTemplate template, HttpSession session) {
        LOG.debug("Updating template with id " + id);
        try {
            RacePositionTemplate templateToUpdate = service.getTemplateForId(id);
            if (template == null) {
                LOG.warn("Failed to get template for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            templateToUpdate.setName(template.getName());
            templateToUpdate.setDefaultTemplate(template.getDefaultTemplate());
            service.updateRacePositionTemplate(templateToUpdate, session.getId());

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Add entry to template, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param entry to add
    //  * @return 201 response with location containing uri of newly created template or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "entry")
    public ResponseEntity<Void> addTemplateEntry(@RequestBody RacePositionTemplateEntry entry, HttpSession session, HttpServletRequest request) {
        LOG.debug("Adding template " + entry.getTemplate().getId() + " at position " + entry.getPosition().getId());
        try {
            Position position = positionService.getPositionForId(entry.getPositionId());
            if (position == null) {
                LOG.warn("Failed to get position for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
            if (template == null) {
                LOG.warn("Failed to get template for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            entry.setTemplate(template);
            RacePositionTemplateEntry newEntry = service.addTemplateEntry(entry, session.getId());
            URI location = URI.create(request.getRequestURI() + "/"+ newEntry.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("location", location.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Remove entry from template, restricted to Admin users
    //  * @param templateId id of template in entry
    //  * @param positionId id of position in entry
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @DeleteMapping(path = "entry/template/{templateId}/position/{positionId}")
    public ResponseEntity<Void> deleteTemplateEntry(@PathVariable("templateId") Long templateId, @PathVariable("positionId") Long positionId, HttpSession session) {
        LOG.debug("Removing template with templateId " + templateId + " and positionId " + positionId);
        try {
            RacePositionTemplateEntry entry = new RacePositionTemplateEntry();
            Position position = positionService.getPositionForId(positionId);
            if (position == null) {
                LOG.warn("Failed to get position for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            entry.setPosition(position);

            RacePositionTemplate template = service.getTemplateForId(templateId);
            if (template == null) {
                LOG.warn("Failed to get template for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            entry.setTemplate(template);
            if (position != null && template != null) {
                service.deleteTemplateEntry(entry, session.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Update entry of a template, restricted to Admin users
    //  * @param entry entry to update
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, path = "entry")
    public ResponseEntity<Void> updateTemplateEntry(@RequestBody RacePositionTemplateEntry entry, HttpSession session) {
        LOG.debug("Updating template entry with id " + entry.getTemplate().getId());
        try {
            Position position = positionService.getPositionForId(entry.getPositionId());
            if (position == null) {
                LOG.warn("Failed to get position for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            entry.setPosition(position);

            RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
            if (template == null) {
                LOG.warn("Failed to get template for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            entry.setTemplate(template);
            
            service.updateTemplateEntry(entry, session.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

}
