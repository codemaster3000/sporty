package at.sporty.team1.application.jms;

import at.sporty.team1.shared.dtos.MessageDTO;
import com.sun.messaging.Queue;
import com.sun.messaging.QueueConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;

/**
 * Created by sereGkaluv on 30-Nov-15.
 */
public class ProducerJMS {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 3700;

    public static void sendMessage(MessageDTO messageDTO)
    throws JMSException {

        QueueConnectionFactory factory = JMSFacade.lookupForQueueConnectionFactory(DEFAULT_HOST, 3700, true);
        JMSContext jmsContext = factory.createContext();

        JMSProducer producer = JMSFacade.getJMSProducer(jmsContext);

        Queue recipientQueue = JMSFacade.lookupForQueue(messageDTO.getRecipientId());
        producer.send(recipientQueue, messageDTO);

        jmsContext.close();
    }
}