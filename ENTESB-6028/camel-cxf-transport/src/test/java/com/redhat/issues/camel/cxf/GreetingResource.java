package com.redhat.issues.camel.cxf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/greeting")
public interface GreetingResource {
    @GET
    @Path("/hello/{name}")
    @Consumes("text/plain")
    @Produces("text/plain")
    String hello(@PathParam("name") String name);
}
