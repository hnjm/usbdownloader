package name.ialert.usbdownloader.logger;

import name.ialert.usbdownloader.logger.jul.CustomFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConsoleLogger extends AbstractLogger implements ILogger {

    private static ConsoleLogger instance;

    private ConsoleHandler consoleHandler;

    private Logger loggerHandler;

    private ConsoleLogger(String className) {

        this.loggerHandler = Logger.getLogger(className);
        this.loggerHandler.setUseParentHandlers(false);

        this.consoleHandler = new ConsoleHandler();

        CustomFormatter formatterHandler = new CustomFormatter();
        this.consoleHandler.setFormatter(formatterHandler);

        this.loggerHandler.addHandler(this.consoleHandler);
    }

    public void setLogLevel(LogLevelType levelType) {

        super.setLogLevel(levelType);

        Level setupLevel = Level.INFO;

        if(levelType == LogLevelType.DEBUG) {

            setupLevel = Level.FINEST;
        }

        this.consoleHandler.setLevel(setupLevel);
        this.loggerHandler.setLevel(setupLevel);

    }

    public static ConsoleLogger getInstance(String className) {

        if(ConsoleLogger.instance == null) ConsoleLogger.instance = new ConsoleLogger(className);

        return ConsoleLogger.instance;
    }

    @Override
    public void info(String message) {

        this.loggerHandler.log(Level.INFO,message);
    }

    @Override
    public void error(String message) {

        this.loggerHandler.log(Level.FINEST,message);
    }

    @Override
    public void warning(String message) {

        this.loggerHandler.log(Level.WARNING,message);
    }
}
