package com.redhat.issues.switchyard.jms1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;

public class PoolingConnectionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PoolingConnectionFactory.class);

    @Produces
    @Named("PoolingConnectionFactory")
    public ConnectionFactory create() {
        // Try 10 times
        for (int i = 0; i < 60; i++) {
            InitialContext context = null;
            try {
                context = new InitialContext();
                return (ConnectionFactory) context.lookup("ConnectionFactory");
            } catch (Exception e) {
                LOG.warn("##### {} - Failed to lookup ConnectionFactory. Retrying #####", i);
            } finally {
                if (context != null) {
                    try {
                        context.close();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return null;
    }

}
