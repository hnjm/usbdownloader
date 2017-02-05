package name.ialert.usbdownloader;


import name.ialert.usbdownloader.logger.AbstractLogger;
import name.ialert.usbdownloader.logger.CurrentLogger;
import net.samuelcampos.usbdrivedectector.USBStorageDevice;
import net.samuelcampos.usbdrivedectector.events.DeviceEventType;
import net.samuelcampos.usbdrivedectector.events.IUSBDriveListener;
import net.samuelcampos.usbdrivedectector.events.USBStorageEvent;

import java.io.File;
import java.util.concurrent.*;

public class DriveListener implements IUSBDriveListener {

    /**
     * Necessary drive name
     */
    protected String driveName;

    /**
     * Drive directory
     */
    protected String downloadDirectory;

    /**
     * Urls to download
     */
    protected  String[] downloadUrls;

    /**
     * Time interval to update
     */
    protected long updateTime;

    /**
     * Max thread to download streams
     */
    protected static int MAX_DOWNLOAD_THREAD = 5;

    /**
     * Default files update interval
     */
    private final static long DEFAULT_UPDATE_TIME = 86400;

    /**
     * Log instance
     */
    private static final AbstractLogger Log = CurrentLogger.getInstance(DriveListener.class.getName());


    /**
     * @param driveName Drive name
     * @param directory Drive directory
     * @param urls Urls to download
     * @param updateTime Time interval to update
     */
    public DriveListener(String driveName,String directory,String[] urls,long updateTime) {

        this.driveName = driveName;
        this.downloadDirectory = directory;
        this.downloadUrls = urls;
        this.updateTime = updateTime > 0 ? updateTime : DEFAULT_UPDATE_TIME;
    }

    /**
     * @param driveName Drive name
     * @param directory Drive directory
     * @param urls Urls to download
     */
    public DriveListener(String driveName,String directory,String[] urls) {

        this.driveName = driveName;
        this.downloadDirectory = directory;
        this.downloadUrls = urls;
        this.updateTime = DEFAULT_UPDATE_TIME;
    }

    /**
     * Drive connect/remove event listener
     * @param event flash drive connect/remove event
     */
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

    /**
     * Download files
     * @param drive drive object
     */
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

    /**
     * Get drive directory path from object
     * @param drive
     * @return drive directory
     */
    protected String getDriveDirectoryPath(USBStorageDevice drive) {

        String path = "";

        String rootDirectory = drive.getRootDirectory().toString();

        path = path.concat(rootDirectory);

        if(this.downloadDirectory != null && this.downloadDirectory.trim().length() > 0) {

            path = path.concat(File.separator).concat(this.downloadDirectory);
        }

        return path;
    }

    /**
     * Check drive name
     * @param drive
     * @return true if drive is necessary
     */
    protected boolean isNecessaryDrive(USBStorageDevice drive) {

        return drive.getDeviceName().equals(this.driveName);
    }
}
