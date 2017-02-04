package name.ialert.usbdownloader.logger;

public interface ILogger {
    /**
     * Add info message to logger
     * @param message
     */
    void info(String message);

    /**
     * Add error message to logger
     * @param message
     */
    void error(String message);

    /**
     * Add warning message to logger
     * @param message
     */
    void warning(String message);
}
