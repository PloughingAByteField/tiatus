package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.role.Role;
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 27/02/2017.
 */
@Path("users")
@SuppressWarnings("squid:S1166")
public class UserRestPoint {
    private static final Logger LOG = LoggerFactory.getLogger(UserRestPoint.class);
    private static final String CACHE_NAME = "users";

    private UserService service;
    private Cache cache;

    /**
     * Get users
     * @return response containing list of users
     */
    @RolesAllowed({Role.ADMIN})
    @GET
    @Produces("application/json")
    public Response getUsers(@Context Request request) {
        try {
            Response.ResponseBuilder builder;
            if (cache.get(CACHE_NAME) != null) {
                CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
                String cachedEntryETag = cacheEntry.getETag();

                EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
                builder = request.evaluatePreconditions(cachedRacesETag);
                if (builder == null) {
                    List<User> users = (List<User>)cacheEntry.getEntry();
                    builder = Response.ok(users).tag(cachedEntryETag);
                }
            } else {
                List<User> users = service.getUsers();
                String hashCode = Integer.toString(users.hashCode());
                EntityTag etag = new EntityTag(hashCode, false);
                CacheEntry newCacheEntry = new CacheEntry(hashCode, users);
                cache.put(CACHE_NAME, newCacheEntry);
                builder = Response.ok(users).tag(etag);
            }

            return builder.build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @RolesAllowed({Role.ADMIN})
    @GET
    @Path("roles")
    @Produces("application/json")
    public Response getUserRoles(@Context Request request) {
        try {
            Response.ResponseBuilder builder;
            String cacheName = CACHE_NAME + "_" + "Roles";
            if (cache.get(cacheName) != null) {
                CacheEntry cacheEntry = (CacheEntry)cache.get(cacheName);
                String cachedEntryETag = cacheEntry.getETag();

                EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
                builder = request.evaluatePreconditions(cachedRacesETag);
                if (builder == null) {
                    List<org.tiatus.entity.Role> userRoles = (List<org.tiatus.entity.Role>)cacheEntry.getEntry();
                    builder = Response.ok(userRoles).tag(cachedEntryETag);
                }
            } else {
                List<org.tiatus.entity.Role> userRoles = service.getUserRoles();
                String hashCode = Integer.toString(userRoles.hashCode());
                EntityTag etag = new EntityTag(hashCode, false);
                CacheEntry newCacheEntry = new CacheEntry(hashCode, userRoles);
                cache.put(cacheName, newCacheEntry);
                builder = Response.ok(userRoles).tag(etag);
            }

            return builder.build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Add user, restricted to Admin users
     * @param uriInfo location details
     * @param user to add
     * @return 201 response with location containing uri of newly created user or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        LOG.debug("Adding user " + user);
        try {
            User saved = service.addUser(user);
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove user, restricted to Admin users
     * @param id of user to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeUser(@PathParam("id") String id) {
        LOG.debug("Removing user with id " + id);
        try {
            User user = new User();
            user.setId(Long.parseLong(id));
            service.deleteUser(user);
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update user, restricted to Admin users
     * @param user to update
     * @return 204 response or an error code
     */
    @PUT
    @RolesAllowed({Role.ADMIN})
    @Produces("application/json")
    public Response updateUser(User user) {
        LOG.debug("updating user");
        try {
            service.updateUser(user);
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @Inject
    public void setService(UserService service) {
        this.service = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
