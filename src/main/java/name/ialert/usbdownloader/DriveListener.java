package name.ialert.usbdownloader;


import name.ialert.usbdownloader.logger.ConsoleLogger;
import net.samuelcampos.usbdrivedectector.USBStorageDevice;
import net.samuelcampos.usbdrivedectector.events.DeviceEventType;
import net.samuelcampos.usbdrivedectector.events.IUSBDriveListener;
import net.samuelcampos.usbdrivedectector.events.USBStorageEvent;

import java.io.File;
import java.util.concurrent.*;

public class DriveListener implements IUSBDriveListener {

    protected static int MAX_DOWNLOAD_THREAD = 5;

    protected String driveName;

    protected String downloadDirectory;

    protected  String[] downloadUrls;

    protected long updateTime;

    protected final static long DEFAULT_UPDATE_TIME = 86400;

    private static final ConsoleLogger Log = ConsoleLogger.getInstance(DriveListener.class.getName());


    public DriveListener(String driveName,String directory,String[] urls,long updateTime) {

        this.driveName = driveName;
        this.downloadDirectory = directory;
        this.downloadUrls = urls;
        this.updateTime = updateTime;
    }

    public DriveListener(String driveName,String directory,String[] urls) {

        this.driveName = driveName;
        this.downloadDirectory = directory;
        this.downloadUrls = urls;
        this.updateTime = DEFAULT_UPDATE_TIME;
    }

    public void usbDriveEvent(USBStorageEvent event) {

        DeviceEventType eventType = event.getEventType();

        if(eventType == DeviceEventType.CONNECTED) {

            USBStorageDevice drive = event.getStorageDevice();

            if(this.isNecessaryDrive(drive)) {

                Log.info("Storage "+drive.getDeviceName()+" was found.");

                this.downloadFiles(drive);
            }
        }


    }

    protected void downloadFiles(USBStorageDevice drive) {

        int filesCount = this.downloadUrls.length;

        if(filesCount > 0) {

            int threadMax = (filesCount > MAX_DOWNLOAD_THREAD) ? MAX_DOWNLOAD_THREAD : filesCount;

            ExecutorService taskExecutor = Executors.newFixedThreadPool(threadMax);

            String driveDirectory = this.getDriveDirectoryPath(drive);

            for (String url : this.downloadUrls) {
                taskExecutor.submit(new FileDownload(url, driveDirectory, this.updateTime));
            }

            taskExecutor.shutdown();

            try {
                taskExecutor.awaitTermination(1, TimeUnit.MINUTES);

            } catch (InterruptedException e) {

                Log.error("Unable to await threads termination"+e.getMessage());
            }

        }
    }


    protected String getDriveDirectoryPath(USBStorageDevice drive) {

        String path = "";

        String rootDirectory = drive.getRootDirectory().toString();

        path = path.concat(rootDirectory);

        if(this.downloadDirectory != null && this.downloadDirectory.trim().length() > 0) {

            path = path.concat(File.separator).concat(this.downloadDirectory);
        }

        return path;
    }

    protected boolean isNecessaryDrive(USBStorageDevice drive) {

        return drive.getDeviceName().equals(this.driveName);
    }
}
