package com.redhat.issues.amq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringJmsConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringJmsConsumer.class);

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

}
