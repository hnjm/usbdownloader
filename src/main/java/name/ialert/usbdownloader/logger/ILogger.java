package name.ialert.usbdownloader.logger;

public interface ILogger {
    void info(String message);
    void error(String message);
    void warning(String message);
}
