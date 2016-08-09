package com.redhat.issues.cxf.jms;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.soap.SOAPFaultException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GreetingClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingClient.class);

    @Test(expected = SOAPFaultException.class)
    public void invoke() {
        GreetingService client = (GreetingService) proxyFactory().create();
        String message = client.hello("GreetingClient");
        LOGGER.info(message);
        assertThat(message, is("Hello, GreetingClient!"));
    }

    @Test(expected = SOAPFaultException.class)
    public void invoke_spring() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:client.xml");
        GreetingService client = (GreetingService) context.getBean("greetingClient");
        String message = client.hello("GreetingClient");
        LOGGER.info(message);
        assertThat(message, is("Hello, GreetingClient!"));
    }

    private JaxWsProxyFactoryBean proxyFactory() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(GreetingService.class);
        factory.setAddress("jms://");
        factory.getFeatures().add(jmsConfigFeature());
        return factory;
    }

    private JMSConfigFeature jmsConfigFeature() {
        JMSConfigFeature feature = new JMSConfigFeature();
        JMSConfiguration config = Server.jmsConfiguration();
        config.setReceiveTimeout(1000L);
        config.setReplyDestination(Server.QUEUE_RESPONSE);
        config.setReplyToDestination(Server.QUEUE_RESPONSE_2);
        feature.setJmsConfig(config);
        return feature;
    }

}
