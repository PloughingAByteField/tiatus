package org.tiatus.server.filter;

// import org.infinispan.Cache;
// import org.infinispan.CacheSet;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import javax.annotation.Priority;
// import javax.inject.Inject;
// import javax.ws.rs.HttpMethod;
// import javax.ws.rs.Priorities;
// import javax.ws.rs.container.ContainerRequestContext;
// import javax.ws.rs.container.ContainerResponseContext;
// import javax.ws.rs.container.ContainerResponseFilter;
// import javax.ws.rs.core.EntityTag;
// import javax.ws.rs.core.Response;
// import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by johnreynolds on 26/08/2016.
 */
// @Provider
// @Priority(Priorities.HEADER_DECORATOR)
public class CacheResponseFilter 
// implements ContainerResponseFilter 
{
    // private static final Logger LOG = LoggerFactory.getLogger(CacheResponseFilter.class);
    // private Cache cache;

    // @Override
    // public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
    //     String uri = request.getUriInfo().getPath();
    //     if (request.getMethod().equals(HttpMethod.GET)) {
    //         if (response.getStatus() != Response.Status.OK.getStatusCode()) {
    //             return;
    //         }

    //         // fill cache if it is empty
    //         if (cache.get(uri) == null && response.getEntity() != null) {
    //             Object entity = response.getEntity();
    //             String hashCode = Integer.toString(entity.hashCode());
    //             EntityTag eTag = new EntityTag(hashCode, false);
    //             CacheEntry newCacheEntry = new CacheEntry(hashCode, entity);
    //             LOG.debug("Adding cache for " + uri);
    //             cache.put(uri, newCacheEntry);
    //             response.getHeaders().add("ETag", eTag.toString());
    //         }
    //     } else {
    //         // empty caches
    //         CacheSet<String> keys = cache.keySet();
    //         for (String key: keys) {
    //             if (uri.contains(key) && cache.get(key) != null) {
    //                 LOG.debug("Evicting " + key + " for uri " + uri);
    //                 cache.evict(key);
    //             }
    //         }
    //     }
    // }

    // // @Inject
    // public void setCache(Cache cache) {
    //     this.cache = cache;
    // }
}
