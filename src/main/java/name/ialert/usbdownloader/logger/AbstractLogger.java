package name.ialert.usbdownloader.logger;

public abstract class AbstractLogger {


    private static LogLevelType DEFAULT_LOG_LEVEL = LogLevelType.INFO;

    private LogLevelType logLevel;

    public LogLevelType getLogLevel() {

        if(this.logLevel == null) this.setLogLevel(DEFAULT_LOG_LEVEL);

        return this.logLevel;
    }

    public void setLogLevel(LogLevelType logLevel) {

       this.logLevel = logLevel;
    }
}
