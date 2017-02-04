package name.ialert.usbdownloader;


import name.ialert.usbdownloader.logger.ConsoleLogger;
import name.ialert.usbdownloader.logger.LogLevelType;
import net.samuelcampos.usbdrivedectector.USBDeviceDetectorManager;
import org.apache.commons.cli.*;

public class UsbDownloader {

    /**
     * Thread wake up interval
     */
    private static int THREAD_SLEEP_TIME = 5000;

    /**
     * Supported CLI options
     */
    private Options options;

    /**
     * Necessary drive name
     */
    private String driveName;

    /**
     * Drive directory
     */
    private String driveDirectory;

    /**
     * Urls to download
     */
    private  String[] downloadUrls;

    /**
     * Log Instance
     */
    private static final ConsoleLogger Log = ConsoleLogger.getInstance(UsbDownloader.class.getName());


    public static void main(String[] args) {

        new UsbDownloader(args);

    }

    /**
     * @param args CLI args
     */
    public UsbDownloader(String[] args) {

        this.options = new Options();

        this.addOption("d","drive","Drive name");
        this.addOption("dir","directory","Drive directory");
        this.addOption("u","urls","Download url");
        this.addOption("debug","enable-debug",false,"Enable debug log");

        if(this.parseArguments(args)) {

            USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();

            DriveListener dListener = new DriveListener(this.driveName,this.driveDirectory,this.downloadUrls);

            driveDetector.addDriveListener(dListener);

            this.setThreadSleep();
        }

    }

    /**
     *
     * @param optionKey CLI option key
     * @param longOptionKey CLI option long key
     * @param optionDescription CLI option description
     */
    public void addOption(String optionKey,String longOptionKey,String optionDescription) {

        this.addOption(optionKey,longOptionKey, true, optionDescription);
    }

    /**
     *
     * @param optionKey CLI option key
     * @param longOptionKey CLI option long key
     * @param hasArgs  CLI option is boolean
     * @param optionDescription CLI option description
     */
    public void addOption(String optionKey,String longOptionKey,boolean hasArgs,String optionDescription) {

        this.options.addOption(optionKey,longOptionKey, hasArgs, optionDescription);
    }

    /**
     * Set main thread sleep
     */
    private void setThreadSleep() {

        while(true) {

            try {
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {

                Log.warning("Error was occurred."+e.getMessage());

            }
        }
    }

    /**
     *
     * @param args CLI options
     * @return true if parsing is succeeded
     */
    private boolean parseArguments(String args[]) {

        boolean error = true;

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(this.options, args);

            boolean isDebug = cmd.hasOption("debug");

            if(isDebug) Log.setLogLevel(LogLevelType.DEBUG);

            this.driveName = cmd.getOptionValue("d");
            this.downloadUrls = cmd.getOptionValues("u");

            if(this.driveName == null || this.driveName.isEmpty()) {

                Log.info("You must set target drive name");

            } else if(this.downloadUrls == null || this.downloadUrls.length == 0) {

                Log.info("You must set at least one url");
            } else {

                this.driveDirectory = cmd.getOptionValue("dir");

                error = false;

            }
        } catch (ParseException e) {

            Log.warning("Unable to parse program args.");
            Log.error(e.getMessage());
        }

        return !error;
    }

}