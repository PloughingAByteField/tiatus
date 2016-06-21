package org.tiatus.server.rest;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("race")
public class Race {

    @PermitAll
    @Produces("application/json")
    public Response getRaces() {
        return Response.ok().build();
    }
}
