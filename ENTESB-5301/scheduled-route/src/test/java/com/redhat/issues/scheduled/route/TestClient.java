package com.redhat.issues.scheduled.route;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClient implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String DEFAULT_QUEUE = "TEST";
    private static final String QUEUE_PROPERTY_IN = "test.queue.in";
    private static final String QUEUE_PROPERTY_OUT = "test.queue.out";

    private static final long MESSAGE_INTERVAL = 5 * 1000;
    private static final int MESSAGE_COUNT = 100;

    private Destination in;

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private long counter = 0;

    public TestClient() throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection("admin", "admin");
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        in = session.createQueue(queueNameIn());
        Destination out = session.createQueue(queueNameOut());
        consumer = session.createConsumer(out);
        consumer.setMessageListener(this);
    }

    private static String queueNameIn() {
        String name = System.getProperty(QUEUE_PROPERTY_IN);
        if (name != null) return name;
        return DEFAULT_QUEUE;
    }

    private static String queueNameOut() {
        String name = System.getProperty(QUEUE_PROPERTY_OUT);
        if (name != null) return name;
        return DEFAULT_QUEUE + "-out";
    }

    public void send(int messageCount) throws JMSException {
        MessageProducer producer = null;
        try {
            producer = session.createProducer(in);
            for (int i = 0; i < messageCount; i++) {
                counter++;
                TextMessage text = session.createTextMessage("Test: " + counter);
                producer.send(text);
            }
        } finally {
            if (producer != null) producer.close();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            LOGGER.info("{}: {}",
                    new SimpleDateFormat("HH:mm:ss,SSS").format(new Date()),
                    text.split("\n").length);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            if (consumer != null) consumer.close();
            if (connection != null) connection.stop();
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        TestClient client = null;
        try {
            client = new TestClient();
            while (true) {
                client.send(MESSAGE_COUNT);
                Thread.sleep(MESSAGE_INTERVAL);
            }
        } finally {
            if (client != null) client.close();
        }
    }

}
