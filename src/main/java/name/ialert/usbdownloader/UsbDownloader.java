package name.ialert.usbdownloader;


import net.samuelcampos.usbdrivedectector.USBDeviceDetectorManager;
import org.apache.commons.cli.*;

public class UsbDownloader {

    private static int THREAD_SLEEP_TIME = 5000;

    private Options options;

    private String driveName;

    private String driveDirectory;

    private  String[] downloadUrls;


    public static void main(String[] args) {

        new UsbDownloader(args);

    }

    public UsbDownloader(String[] args) {

        this.options = new Options();

        this.addOption("d","drive","Drive name");
        this.addOption("dir","directory","Drive directory");
        this.addOption("u","urls","Download url");

        if(this.parseArguments(args)) {

            USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();

            DriveListener dListener = new DriveListener(this.driveName,this.driveDirectory,this.downloadUrls);

            driveDetector.addDriveListener(dListener);

            this.setThreadSleep();
        }

    }

    private void setThreadSleep() {

        while(true) {

            try {
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {

                Logger.add("Error was occurred."+e.getMessage(),true);

            }
        }
    }

    private boolean parseArguments(String args[]) {

        boolean error = true;

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(this.options, args);

            this.driveName = cmd.getOptionValue("d");
            this.downloadUrls = cmd.getOptionValues("u");

            if(this.driveName == null || this.driveName.isEmpty()) {

                Logger.add("You must set target drive name",true);

            } else if(this.downloadUrls == null || this.downloadUrls.length == 0) {

                Logger.add("You must set at least one url",true);
            } else {

                this.driveDirectory = cmd.getOptionValue("dir");

                error = false;

            }


        } catch (ParseException e) {

            Logger.add("Unable to parse program args."+e.getMessage(),true);
        }

        return !error;
    }

    public void addOption(String optionKey,String longOptionKey,String optionDescription) {

        this.options.addOption(optionKey,longOptionKey, true, optionDescription);
    }

}