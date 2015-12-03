package at.sporty.team1.application.jms;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.Queue;
import com.sun.messaging.QueueConnectionFactory;

import javax.jms.*;

/**
 * Created by sereGkaluv on 03-Dec-15.
 */
public class JMSFacade {
    private static final String ADDRESS_DELIMITER = ":";
    private static final String BOOL_TRUE = "true";
    private static final String BOOL_FALSE = "false";
    private static final String JMS_PREFIX = "jms/";

    private JMSFacade() {
    }

    public static QueueConnectionFactory lookupForQueueConnectionFactory(String host, int port, boolean reconnect)
    throws JMSException {
        QueueConnectionFactory connectionFactory = new QueueConnectionFactory();
        connectionFactory.setProperty(ConnectionConfiguration.imqAddressList, host + ADDRESS_DELIMITER + port);
        connectionFactory.setProperty(ConnectionConfiguration.imqReconnectEnabled, readBoolean(reconnect));

        return connectionFactory;
    }

    public static Queue lookupForQueue(Integer memberId)
    throws JMSException, IllegalArgumentException {

        if (memberId != null) return new Queue(JMS_PREFIX + memberId.toString());
        else throw new IllegalArgumentException();
    }

    public static JMSProducer getJMSProducer(JMSContext jmsContext) {
        return jmsContext != null ? jmsContext.createProducer() : null;
    }

    public static JMSConsumer getJMSConsumer(Queue queue, JMSContext jmsContext) {
        return jmsContext != null ? jmsContext.createConsumer(queue) : null;
    }

    private static String readBoolean(boolean isValue) {
        return isValue ? BOOL_TRUE : BOOL_FALSE;
    }
}
