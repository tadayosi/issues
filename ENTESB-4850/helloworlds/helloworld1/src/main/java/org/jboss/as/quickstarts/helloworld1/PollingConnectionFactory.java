package org.jboss.as.quickstarts.helloworld1;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;

public class PollingConnectionFactory {

    @Produces
    @Named("PollingConnectionFactory")
    public ConnectionFactory create() {
        System.out.println("##### Creating " + PollingConnectionFactory.class.getSimpleName() + " #####");
        // Try 10 times
        for (int i = 0; i < 30; i++) {
            InitialContext context = null;
            try {
                context = new InitialContext();
                return (ConnectionFactory) context.lookup("ConnectionFactory");
            } catch (Exception e) {
                System.err.println("##### " + i + " - Failed to lookup ConnectionFactory. Retrying #####");
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
