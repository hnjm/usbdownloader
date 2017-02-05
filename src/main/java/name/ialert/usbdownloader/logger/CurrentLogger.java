package name.ialert.usbdownloader.logger;


import name.ialert.usbdownloader.logger.console.ConsoleLogger;

public class CurrentLogger {

    /**
     * Get instance of current logging class.Default to ConsoleLogger
     * @param classname log class name
     * @return instance of class
     */
    public static AbstractLogger getInstance(String classname) {

        return ConsoleLogger.getInstance(classname);
    }
}
