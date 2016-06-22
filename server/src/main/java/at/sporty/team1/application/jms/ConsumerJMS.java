package at.sporty.team1.application.jms;

/**
 * Created by sereGkaluv on 30-Nov-15.
 */
public class ConsumerJMS {
    /*private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 7676;
    private static final int TIMEOUT = 5000;

    public static MessageDTO pullMessage(Integer memberId)
    throws JMSException, NamingException {

        ConnectionFactory factory = JMSFacade.lookupForConnectionFactory(DEFAULT_HOST, DEFAULT_PORT, true);
        Queue consumerQueue = JMSFacade.lookupForQueue(memberId);

        Connection connection = factory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();

        MessageConsumer consumer = session.createConsumer(consumerQueue);

        ObjectMessage message  = (ObjectMessage) consumer.receive(TIMEOUT);
        connection.close();

        return message != null ? (MessageDTO) message.getObject() : null;
    }*/
}
