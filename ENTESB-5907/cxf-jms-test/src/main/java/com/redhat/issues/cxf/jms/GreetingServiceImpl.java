package com.redhat.issues.cxf.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;

@WebService(
    name = "GreetingService",
    serviceName = "GreetingService",
    portName = "GreetingServicePort",
    endpointInterface = "com.redhat.issues.cxf.jms.GreetingService")
public class GreetingServiceImpl implements GreetingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServiceImpl.class);

    public String hello(String name) {
        LOGGER.info("name = {}", name);
        return String.format("Hello, %s!", name);
    }

}
