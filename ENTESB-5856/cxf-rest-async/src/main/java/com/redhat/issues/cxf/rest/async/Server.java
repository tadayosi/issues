package com.redhat.issues.cxf.rest.async;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

public class Server {

    public static final String ADDRESS = "http://localhost:9000/";

    public static void main(String[] args) {
        try {
            serverFactory().create();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static JAXRSServerFactoryBean serverFactory() {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setResourceClasses(GreetingService.class);
        factory.setAddress(ADDRESS);
        factory.getFeatures().add(new LoggingFeature());
        return factory;
    }

}
