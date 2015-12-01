package at.sporty.team1.application.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 * Created by sereGkaluv on 30-Nov-15.
 * TODO
 */
public class SenderJMS {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            new SenderJMS().sendToQueue("java:jboss/exported/jms/queue/2004", "message");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a msgObj to a Queue given by String
     * TODO test
     *
     * @param quename
     * @param msgObj
     * @throws NamingException
     * @throws JMSException
     */
    public void sendToQueue(String quename, Object msgObj) throws NamingException, JMSException {

        Queue queue;
        Context ctx = null;
        QueueConnection connect = null;
        QueueSession session = null;
        QueueSender sender = null;

        try {

            ctx = new InitialContext();

            QueueConnectionFactory fact = (QueueConnectionFactory) ctx.lookup("java:jboss/exported/jms/sportyJmsFactory");
            connect = fact.createQueueConnection();
            session = connect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            try {

                queue = (Queue) ctx.lookup(quename);

            } catch (NameNotFoundException ex) {
                queue = session.createQueue(quename);
                ctx.bind(quename, queue);
            }

            sender = session.createSender(queue);
            connect.start();

            //TODO sends 10 times the same message for testing
            for (int i = 0; i < 10; i++) {

                TextMessage msg = session.createTextMessage();
                msg.setText("Die " + (i + 1) + ". Meldung des MyQueueSenders: " + msgObj);

                //abschicken
                sender.send(msg);
               LOGGER.info("Sending " + msg.getText());

            }

        } finally {
            try {
                if (null != sender) sender.close();
            } catch (Exception ex) {/*ok*/}
            try {
                if (null != session) session.close();
            } catch (Exception ex) {/*ok*/}
            try {
                if (null != connect) connect.close();
            } catch (Exception ex) {/*ok*/}
            try {
                if (null != ctx) ctx.close();
            } catch (Exception ex) {/*ok*/}
        }
    }
}
