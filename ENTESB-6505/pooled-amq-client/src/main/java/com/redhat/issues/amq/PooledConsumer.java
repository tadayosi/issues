package com.redhat.issues.amq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.google.common.base.Strings;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PooledConsumer.class);

    //private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String BROKER_URL = "failover:(tcp://localhost:61616)";
    public static final String USERNAME = "test";
    public static final String PASSWORD = "test";
    private static final String QUEUE = "TEST";

    private ConnectionFactory factory;
    private Connection connection;

    public PooledConsumer() throws JMSException {
        PooledConnectionFactory factory = new PooledConnectionFactory(BROKER_URL);
        factory.setMaxConnections(10);
        this.factory = factory;
        init();
    }

    private void init() throws JMSException {
        if (connection != null) {
            close();
        }
        connection = factory.createConnection(USERNAME, PASSWORD);
        connection.start();
    }

    public void listen() {
        Session session = null;
        MessageConsumer consumer = null;
        boolean success = true;
        while (true) {
            try {
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination queue = session.createQueue(QUEUE);
                consumer = session.createConsumer(queue);
                onMessage(consumer.receive());
                success = true;
            } catch (JMSException e) {
                LOGGER.warn(e.getMessage(), e);
                success = false;
            } finally {
                try {
                    if (consumer != null) consumer.close();
                    if (session != null) session.close();
                    if (!success) init();
                } catch (JMSException ignore) {}
            }
            sleep(1);
        }
    }

    private static void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {}
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String response = textMessage.getText();
            LOGGER.info(Strings.repeat("=", 50));
            LOGGER.info("Received: '{}'", response);
            LOGGER.info(Strings.repeat("=", 50));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            if (connection != null) {
                //connection.stop();
                connection.close();
            }
        } catch (JMSException e) {}
    }

    public static void main(String[] args) throws Exception {
        PooledConsumer consumer = null;
        try {
            consumer = new PooledConsumer();
            consumer.listen();
            //Thread.sleep(1000);
        } finally {
            if (consumer != null) consumer.close();
        }
    }

}
