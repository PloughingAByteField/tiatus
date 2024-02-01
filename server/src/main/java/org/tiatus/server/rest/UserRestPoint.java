package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.role.Role;
import org.tiatus.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.annotation.security.RolesAllowed;

import java.net.URI;
import java.util.ArrayList;
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
 * Created by johnreynolds on 27/02/2017.
 */
// @Path("users")
// @SuppressWarnings("squid:S1166")
@RestController
@RequestMapping("/rest/users")
public class UserRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(UserRestPoint.class);

    @Autowired
    protected UserService service;

    // /**
    //  * Get users
    //  * @return response containing list of users
    //  */
    @RolesAllowed(Role.ADMIN)
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<User> getUsers() {
        try {
            return service.getUsers();

        } catch (Exception e) {
            logError(e);
        }

        return new ArrayList<>();
    }

    @RolesAllowed(Role.ADMIN)
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "roles")
    public List<org.tiatus.entity.Role> getUserRoles() {
        try {
            return service.getUserRoles();

        } catch (Exception e) {
            logError(e);
        }

        return new ArrayList<>();
    }

    // /**
    //  * Add user, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param user to add
    //  * @return 201 response with location containing uri of newly created user or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> addUser(@RequestBody User user, HttpSession session, HttpServletRequest request) {
        try {
            User newUser = service.addUser(user, session.getId());
            URI location = URI.create(request.getRequestURI() + "/"+ newUser.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("location", location.toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }

    // /**
    //  * Remove user, restricted to Admin users
    //  * @param id of user to remove
    //  * @return response with 204
    //  */
    @RolesAllowed(Role.ADMIN)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void removeUser(@PathVariable("id") Long id, HttpSession session) {
        LOG.debug("Removing user with id " + id);
        try {
            User user = service.getUserForId(id);
            if (user != null) {
                service.deleteUser(user, session.getId());
            }

        } catch (Exception e) {
            logError(e);
        }
    }

    // /**
    //  * Update user, restricted to Admin users
    //  * @param id of user to update
    //  * @param user to update
    //  * @return 204 response or an error code
    //  */
    @RolesAllowed(Role.ADMIN)
    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }, path = "{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody User user, HttpSession session) {
        LOG.debug("updating user");
        try {
            User existing = service.getUserForId(id);
            if (existing == null) {
                LOG.warn("Failed to get user for supplied id");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            service.updateUser(user, session.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);


        } catch (Exception e) {
            logError(e);
        }

        return ResponseEntity.internalServerError().build();
    }
}
