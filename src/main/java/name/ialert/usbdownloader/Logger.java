package name.ialert.usbdownloader;

import java.util.ArrayList;


public class Logger {

    private static ArrayList<String> messages;

    public static void add(String message) {

        Logger.add(message,false);
    }

    public static void add(String message,boolean isShow) {

        if(!message.isEmpty()) {

            if(Logger.messages == null) {

                Logger.messages = new ArrayList<>();
            }

            Logger.messages.add(message);
        }

        if(isShow) Logger.output();
    }

    public static void output() {

        if(Logger.messages != null && !Logger.messages.isEmpty()) {

            for (String message : Logger.messages) {
                System.out.println(message);
            }

            Logger.clearLog();
        }
    }

    protected static void clearLog() {

        Logger.messages = null;
    }
}
