package at.sporty.team1.application.jms;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Queue;

import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 * Created by sereGkaluv on 03-Dec-15.
 */
public class JMSFacade {
    private static final String ADDRESS_DELIMITER = ":";
    private static final String BOOL_TRUE = "true";
    private static final String BOOL_FALSE = "false";
    private static final String JMS_PREFIX = "Q_";

    private JMSFacade() {
    }

    public static ConnectionFactory lookupForConnectionFactory(String host, int port, boolean reconnect)
    throws JMSException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setProperty(ConnectionConfiguration.imqAddressList, host + ADDRESS_DELIMITER + port);
        connectionFactory.setProperty(ConnectionConfiguration.imqReconnectEnabled, readBoolean(reconnect));

        return connectionFactory;
    }

    public static Queue lookupForQueue(Integer memberId)
    throws JMSException, IllegalArgumentException, NamingException {
        if (memberId == null) throw new IllegalArgumentException();
        return new Queue(JMS_PREFIX + memberId.toString());
    }

    private static String readBoolean(boolean isValue) {
        return isValue ? BOOL_TRUE : BOOL_FALSE;
    }
}
