package com.redhat.issues.camel.cxf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/greeting")
public class GreetingResourceBean implements GreetingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingResourceBean.class);

    @GET
    @Path("/hello/{name}")
    @Consumes("text/plain")
    @Produces("text/plain")
    public String hello(@PathParam("name") String name) {
        LOGGER.info("name = {}", name);
        return String.format("Hello, %s!", name);
    }

}
