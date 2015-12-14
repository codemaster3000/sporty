package at.sporty.team1.application.jms;

import at.sporty.team1.shared.dtos.MessageDTO;
import com.sun.messaging.Queue;
import com.sun.messaging.QueueConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sereGkaluv on 30-Nov-15.
 */
public class ConsumerJMS {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 3700;

    public static List<MessageDTO> pullMessages(Integer memberId)
    throws JMSException {

        QueueConnectionFactory factory = JMSFacade.lookupForQueueConnectionFactory(DEFAULT_HOST, DEFAULT_PORT, true);
        JMSContext jmsContext = factory.createContext();

        Queue consumerQueue = JMSFacade.lookupForQueue(memberId);

        JMSConsumer consumer = JMSFacade.getJMSConsumer(consumerQueue, jmsContext);

        List<MessageDTO> messages = new LinkedList<>();
        MessageDTO tempMessage;

        do {

            tempMessage = (MessageDTO) ((ObjectMessage) consumer.receive()).getObject();
            if (tempMessage != null) messages.add(tempMessage);

        } while (tempMessage != null);

        jmsContext.close();

        return messages;
    }
}
