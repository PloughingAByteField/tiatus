package org.tiatus.server.filter;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.infinispan.Cache;

/**
 * Created by johnreynolds on 05/04/2017.
 */
@ApplicationScoped
public class RestCacheBean {
    @Produces
    @Resource(name="RestCache")
    private Cache cache;

}
