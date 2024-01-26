package org.tiatus.server.filter;

// import org.infinispan.Cache;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import javax.annotation.Priority;
// import javax.inject.Inject;
// import javax.ws.rs.HttpMethod;
// import javax.ws.rs.Priorities;
// import javax.ws.rs.container.ContainerRequestContext;
// import javax.ws.rs.container.ContainerRequestFilter;
// import javax.ws.rs.core.EntityTag;
// import javax.ws.rs.core.Response;
// import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

/**
 * Created by johnreynolds on 26/08/2016.
 */
// @Provider
// @Priority(Priorities.HEADER_DECORATOR)
public class CacheRequestFilter 
// implements ContainerRequestFilter 
{
    // private static final Logger LOG = LoggerFactory.getLogger(CacheRequestFilter.class);
    // private Cache cache;

    // @Override
    // public void filter(ContainerRequestContext request) throws IOException {
    //     if (request.getMethod().equals(HttpMethod.GET)) {
    //         // use cache entry if it exists and cache-control is not set to no-cache
    //         Response.ResponseBuilder builder;
    //         String uri = request.getUriInfo().getPath();
    //         List<String> cacheControlValues = request.getHeaders().get("Cache-Control");
    //         if (cacheControlValues != null && cacheControlValues.contains("no-cache")) {
    //             return;
    //         }

    //         if (cache.get(uri) != null) {
    //             CacheEntry cacheEntry = (CacheEntry) cache.get(uri);
    //             String cachedEntryETag = cacheEntry.getETag();

    //             EntityTag cachedETag = new EntityTag(cachedEntryETag, false);
    //             builder = request.getRequest().evaluatePreconditions(cachedETag);
    //             if (builder != null) {
    //                 request.abortWith(builder.build());
    //             } else {
    //                 // have cached entry
    //                 request.abortWith(Response.ok().entity(cacheEntry.getEntry()).tag(cachedEntryETag).build());
    //             }
    //         }
    //     }
    // }

    // // @Inject
    // public void setCache(Cache cache) {
    //     this.cache = cache;
    // }
}
