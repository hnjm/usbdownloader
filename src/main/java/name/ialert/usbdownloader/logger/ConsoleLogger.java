package name.ialert.usbdownloader.logger;

import name.ialert.usbdownloader.logger.jul.CustomFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConsoleLogger extends AbstractLogger implements ILogger {
    /**
     * Singleton Instance
     */
    private static ConsoleLogger instance;

    /**
     * Console handler from java.util.logging
     */
    private ConsoleHandler consoleHandler;

    /**
     * Logger handler from java.util.logging
     */
    private Logger loggerHandler;


    private ConsoleLogger(String className) {

        this.loggerHandler = Logger.getLogger(className);
        this.loggerHandler.setUseParentHandlers(false);

        this.consoleHandler = new ConsoleHandler();

        CustomFormatter formatterHandler = new CustomFormatter();
        this.consoleHandler.setFormatter(formatterHandler);

        this.loggerHandler.addHandler(this.consoleHandler);
    }

    /**
     * Set logger log level
     * @param logLevel
     */
    public void setLogLevel(LogLevelType logLevel) {

        super.setLogLevel(logLevel);

        Level setupLevel = Level.INFO;

        if(logLevel == LogLevelType.DEBUG) {

            setupLevel = Level.FINEST;
        }

        this.consoleHandler.setLevel(setupLevel);
        this.loggerHandler.setLevel(setupLevel);

    }

    /**
     * Get class instance.Uses for singleton.
     * @param className
     * @return
     */
    public static ConsoleLogger getInstance(String className) {

        if(ConsoleLogger.instance == null) ConsoleLogger.instance = new ConsoleLogger(className);

        return ConsoleLogger.instance;
    }

    /**
     * Add info message to logger
     * @param message
     */
    @Override
    public void info(String message) {

        this.loggerHandler.log(Level.INFO,message);
    }

    /**
     * Add error message to logger
     * @param message
     */
    @Override
    public void error(String message) {

        this.loggerHandler.log(Level.FINEST,message);
    }

    /**
     * Add warning message to logger
     * @param message
     */
    @Override
    public void warning(String message) {

        this.loggerHandler.log(Level.WARNING,message);
    }
}
