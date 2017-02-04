package name.ialert.usbdownloader.logger;

public abstract class AbstractLogger {

    private static LogLevelType DEFAULT_LOG_TYPE = LogLevelType.INFO;

    private LogLevelType logLevel;

    public LogLevelType getLogLevel() {

        if(this.logLevel == null) this.setLogLevel(DEFAULT_LOG_TYPE);

        return this.logLevel;
    }

    public void setLogLevel(LogLevelType levelType) {

       this.logLevel = levelType;
    }
}
