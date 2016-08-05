package com.redhat.issues.cxf.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;

import javax.jms.ConnectionFactory;

public class Server {

    public static final String QUEUE_REQUEST = "sample.ws.greeting.request";
    public static final String QUEUE_RESPONSE = "sample.ws.greeting.response";
    public static final String QUEUE_RESPONSE_2 = "sample.ws.greeting.response2";

    public static final String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        try {
            serverFactory().create();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static JaxWsServerFactoryBean serverFactory() {
        GreetingServiceImpl service = new GreetingServiceImpl();
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(GreetingService.class);
        factory.setAddress("jms://");
        factory.setServiceBean(service);
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.getFeatures().add(jmsConfigFeature());
        return factory;
    }

    public static JMSConfigFeature jmsConfigFeature() {
        JMSConfigFeature feature = new JMSConfigFeature();
        feature.setJmsConfig(jmsConfiguration());
        return feature;
    }

    private static JMSConfiguration jmsConfiguration() {
        JMSConfiguration config = new JMSConfiguration();
        config.setRequestURI(QUEUE_REQUEST);
        config.setConnectionFactory(connectionFactory());
        config.setTargetDestination(QUEUE_REQUEST);
        //config.setReplyDestination(QUEUE_RESPONSE);
        //config.setReplyToDestination(QUEUE_RESPONSE_2);
        return config;
    }

    private static ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory(BROKER_URL);
        amqFactory.setUserName("admin");
        amqFactory.setPassword("admin");
        //return new SingleConnectionFactory(amqFactory);
        return amqFactory;
    }

}
