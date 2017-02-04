package name.ialert.usbdownloader;

import name.ialert.usbdownloader.logger.CustomFormatter;
import name.ialert.usbdownloader.logger.ILogger;
import name.ialert.usbdownloader.logger.StandardLogger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;


public class ConsoleLogger implements ILogger {

    private static ConsoleLogger instance;

    private Logger loggerHandler;

    private ConsoleLogger(String className) {

        this.loggerHandler = StandardLogger.getLogger(className);

        ConsoleHandler customHandler = new ConsoleHandler();
        CustomFormatter customFormatter = new CustomFormatter();
        customHandler.setFormatter(customFormatter);

        this.loggerHandler.addHandler(customHandler);
    }

    public static ConsoleLogger getInstance(String className) {

        if(ConsoleLogger.instance == null) ConsoleLogger.instance = new ConsoleLogger(className);

        return ConsoleLogger.instance;
    }

    @Override
    public void error(String message) {

        this.warning(message);
    }

    @Override
    public void warning(String message) {

    }

    @Override
    public void warning(String className, String Message) {

    }
}
