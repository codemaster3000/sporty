import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.Date;

/**
 * Represents a class with predefined loggers.
 * Created by Jasper Reichardt on 05.04.2015.
 */


/*TODO: log4j lib */
public class Loggers {
    //TODO determine which loggers we want to use
    public static final Logger STANDARD = LogManager.getLogger("STANDARD");
    public static final Logger DEBUG = LogManager.getLogger("DEBUG");
    public static final Logger APPLICATION = LogManager.getLogger("APPLICATION");
    public static final Logger DATABASE = LogManager.getLogger("DATABASE");
    public static final Logger GUI = LogManager.getLogger("GUI");
    public static final Logger USECASE = LogManager.getLogger("USECASE");
    //public static final Logger GWT = LogManager.getLogger("GWT");

    public static void tieSystemOutAndErrToLog() {
        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));
    }

    public static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                DEBUG.error(new Date() + "stdout/err: " + string);
                STANDARD.error(new Date() + "stdout/err: " + string);
            }

            public void println(final String string) {
                DEBUG.error(new Date() + "stdout/err: " + string);
                STANDARD.error(new Date() + "stdout/err: " + string);
            }
        };
    }
}