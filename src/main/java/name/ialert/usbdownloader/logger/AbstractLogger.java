package name.ialert.usbdownloader.logger;

public abstract class AbstractLogger {

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
}
