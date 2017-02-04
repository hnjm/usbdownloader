package name.ialert.usbdownloader.logger.jul;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class CustomFormatter extends Formatter {

    private final DateFormat dformat = new SimpleDateFormat("dd.MM.yy hh:mm:ss");

    @Override
    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(1000);
        builder.append(dformat.format(new Date(record.getMillis()))).append(": ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }
}
