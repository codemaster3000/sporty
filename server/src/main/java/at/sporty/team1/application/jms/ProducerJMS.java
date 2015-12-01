package at.sporty.team1.application.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.jms.*;
import javax.naming.InitialContext;

/**
 * Created by sereGkaluv on 30-Nov-15.
 */
public class ProducerJMS {
    private static final Logger LOGGER = LogManager.getLogger();

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(lookup = "sporty")private static Queue queue;

    public static void main(String[] args) throws Exception {
        Destination destination = queue;

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage();

        for (int i = 0; i < 5; ++i) {
            message.setText("This is message #" + (i + 1) + " from producer");
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
        }

        producer.send(session.createMessage());

        connection.close();
    }
}