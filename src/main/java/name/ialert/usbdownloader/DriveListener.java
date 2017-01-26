package name.ialert.usbdownloader;


import net.samuelcampos.usbdrivedectector.USBStorageDevice;
import net.samuelcampos.usbdrivedectector.events.DeviceEventType;
import net.samuelcampos.usbdrivedectector.events.IUSBDriveListener;
import net.samuelcampos.usbdrivedectector.events.USBStorageEvent;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriveListener implements IUSBDriveListener {

    protected static int MAX_DOWNLOAD_THREAD = 5;

    protected String driveName;

    protected String downloadDirectory;

    protected  String[] downloadUrls;


    public DriveListener(String driveName,String directory,String[] urls) {

        this.driveName = driveName;
        this.downloadDirectory = directory;
        this.downloadUrls = urls;
    }

    public void usbDriveEvent(USBStorageEvent event) {

        DeviceEventType eventType = event.getEventType();

        if(eventType == DeviceEventType.CONNECTED) {

            USBStorageDevice drive = event.getStorageDevice();

            if(this.isNecessaryDrive(drive)) {


            }
        }


    }
    

    protected String getDriveDirectoryPath(USBStorageDevice drive) {

        String path = "";

        String rootDirectory = drive.getRootDirectory().toString();

        path.concat(rootDirectory);

        if(this.downloadDirectory != null && this.downloadDirectory.trim().length() > 0) {

            path.concat(File.separator).concat(this.downloadDirectory);
        }

        return path;
    }

    protected boolean isNecessaryDrive(USBStorageDevice drive) {

        return drive.getDeviceName().equals(this.driveName);
    }
}
