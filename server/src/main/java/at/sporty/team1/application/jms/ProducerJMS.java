package at.sporty.team1.application.jms;

/**
 * Created by sereGkaluv on 30-Nov-15.
 */
public class ProducerJMS {
    /*private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 7676;

    public static void sendMessage(MessageDTO messageDTO)
    throws JMSException, NamingException {

        ConnectionFactory factory = JMSFacade.lookupForConnectionFactory(DEFAULT_HOST, DEFAULT_PORT, true);
        Queue producerQueue = JMSFacade.lookupForQueue(messageDTO.getRecipientId());

        Connection connection = factory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();

        MessageProducer producer = session.createProducer(producerQueue);

        ObjectMessage message = session.createObjectMessage(messageDTO);
        producer.send(message, DeliveryMode.NON_PERSISTENT, 4, 0);

        connection.close();
    }*/
}