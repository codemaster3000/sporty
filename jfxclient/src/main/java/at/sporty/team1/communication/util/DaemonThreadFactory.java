package at.sporty.team1.communication.util;

import java.util.concurrent.ThreadFactory;

/**
 * Created by sereGkaluv on 08-Dec-15.
 */
public class DaemonThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);

        return thread;
    }
}
