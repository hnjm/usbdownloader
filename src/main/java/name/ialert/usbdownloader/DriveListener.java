package name.ialert.usbdownloader;


import net.samuelcampos.usbdrivedectector.USBStorageDevice;
import net.samuelcampos.usbdrivedectector.events.DeviceEventType;
import net.samuelcampos.usbdrivedectector.events.IUSBDriveListener;
import net.samuelcampos.usbdrivedectector.events.USBStorageEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriveListener implements IUSBDriveListener {

    protected static int MAX_DOWNLOAD_THREAD = 5;

    protected String driveName;

    protected String downloadDirectory;

    protected  String[] downloadUrls;

    protected long updateTime;


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
        this.updateTime = 86400;
    }

    public void usbDriveEvent(USBStorageEvent event) {

        System.out.println(event.getStorageDevice().getDeviceName());

        DeviceEventType eventType = event.getEventType();

        if(eventType == DeviceEventType.CONNECTED) {

            USBStorageDevice drive = event.getStorageDevice();

            if(this.isNecessaryDrive(drive)) {

                this.downloadFiles(drive);
            }
        }


    }

    protected void downloadFiles(USBStorageDevice drive) {

        int filesCount = this.downloadUrls.length;

        if(filesCount > 0) {

            int threadMax = (filesCount > MAX_DOWNLOAD_THREAD) ? MAX_DOWNLOAD_THREAD : filesCount;

            ExecutorService executorService = Executors.newFixedThreadPool(threadMax);

            String driveDirectory = this.getDriveDirectoryPath(drive);

            for (String url : this.downloadUrls) {

                executorService.execute(new FileDownload(url, driveDirectory, this.updateTime));
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
