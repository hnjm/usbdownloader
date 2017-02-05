package name.ialert.usbdownloader.logger;

public abstract class AbstractLogger implements ILogger{

    /**
     * Default Log level.Set to INFO
     */
    private static LogLevelType DEFAULT_LOG_LEVEL = LogLevelType.INFO;

    /**
     * Current Log level
     */
    private LogLevelType logLevel;

    /**
     * Get current log level
     * @return current log level
     */
    public LogLevelType getLogLevel() {

        if(this.logLevel == null) this.setLogLevel(DEFAULT_LOG_LEVEL);

        return this.logLevel;
    }

    /**
     * Set current log level
     * @param logLevel
     */
    public void setLogLevel(LogLevelType logLevel) {

       this.logLevel = logLevel;
    }

    /**
     * Add warning message to logger
     * @param message
     */
    public abstract void warning(String message);

    /**
     * Add info message to logger
     * @param message
     */
    public abstract void info(String message);

    /**
     * Add error message to logger
     * @param message
     */
    public abstract void error(String message);
}
