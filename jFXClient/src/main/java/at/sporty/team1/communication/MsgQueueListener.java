package at.sporty.team1.communication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 * Created by f00 on 01.12.15.
 * TODO review, discuss
 */
public class MsgQueueListener implements MessageListener, Runnable {

    /* logger */
    private static final Logger LOGGER = LogManager.getLogger();

    String _quename;

    /**
     * Constructor
     *
     * @param quename Queue-name to listen to as String
     */
    public MsgQueueListener(String quename) throws Exception {
        this._quename = quename;
    }

    @Override
    public  void run() {

        Context ctx = null;
        QueueConnection connect = null;
        QueueSession session = null;
        Queue queue = null;
        QueueReceiver receiver = null;

        try {

            ctx = new InitialContext();
            QueueConnectionFactory fact = (QueueConnectionFactory) ctx.lookup("java:jboss/exported/jms/sportyJmsFactory");
            connect = fact.createQueueConnection();
            session = connect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            try {

                queue = (Queue) ctx.lookup(_quename);

            } catch (NameNotFoundException ex) {
                queue = session.createQueue(_quename);
                ctx.bind(_quename, queue);
            }

            receiver = session.createReceiver(queue);
            receiver.setMessageListener(new MsgQueueListener(_quename));
            connect.start();

            Thread.sleep(20000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (null != receiver) receiver.close();
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

    /**
     *  OnMessage()
     * @param message
     */
    public void onMessage(Message message) {

        try {

            TextMessage msg = (TextMessage) message;
            // TODO alert with message
            System.out.println(msg.getText());
            message.acknowledge();

        } catch (JMSException ex) {
            System.out.println(ex.getMessage());
        }
    }


}