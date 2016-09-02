package com.redhat.issues.cxf.rest.async;

import org.apache.cxf.continuations.Continuation;
import org.apache.cxf.continuations.ContinuationProvider;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

@Path("/greeting")
public class GreetingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingService.class);

    @Context
    private MessageContext context;

    @GET
    @Path("/hello/{name}")
    public String hello(@PathParam("name") String name) {
        ContinuationProvider provider = (ContinuationProvider) context.get(ContinuationProvider.class.getName());
        Continuation continuation = provider.getContinuation();
        synchronized (continuation) {
            if (continuation.isNew()) {
                LOGGER.info("[New]     name = {}", name);
                continuation.suspend(1000);
                return null;
            } else if (continuation.isPending()) {
                LOGGER.info("[Pending] name = {}", name);
                return null;
            } else {
                LOGGER.info("[Resumed] name = {}", name);
                return String.format("Hello, %s!\n", name);
            }
        }
    }

}
