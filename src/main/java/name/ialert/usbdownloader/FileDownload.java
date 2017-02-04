package name.ialert.usbdownloader;


import name.ialert.usbdownloader.logger.ConsoleLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Callable;

public class FileDownload implements Callable<Void> {

    /**
     * File source path
     */
    protected String filePath;

    /**
     * Destination directory
     */
    protected Path destinationDirectory;

    /**
     * Time interval to update
     */
    protected long updateTime;

    /**
     * Log Instance
     */
    private static final ConsoleLogger Log = ConsoleLogger.getInstance(FileDownload.class.getName());

    /**
     *
     * @param fileUrl File source path
     * @param destinationDirectory Destination directory
     * @param updateTime Time interval to update
     */
    public FileDownload(String fileUrl,String destinationDirectory,long updateTime) {

        this.filePath = fileUrl;
        this.destinationDirectory = this.getPath(destinationDirectory);
        this.updateTime = updateTime * 1000;
    }

    /**
     * method calls by ExecutorService
     */
    public Void call() {

        this.downloadFile();

        return null;
    }

    /**
     * Main method to download file
     */
    public void downloadFile() {

        String filename = this.getFilename(this.filePath);
        Path target = this.destinationDirectory.resolve(filename.toString());

        if(this.isNeedUpdateFile(target)) {

            if (this.checkPathWritable(this.destinationDirectory)) {

                URL downloadUrl;

                try {

                    Log.info("Start downloading "+this.filePath);

                    downloadUrl = new URL(this.filePath);

                    try (InputStream in = downloadUrl.openStream()) {

                        Log.info("Download finished."+this.filePath);

                        this.copyFile(in, target);

                        in.close();

                    } catch (IOException e) {

                        Log.warning("Unable to download file "+this.filePath+".");
                        Log.error(e.getMessage());
                    }

                } catch (MalformedURLException e) {

                    Log.warning("Incorrect url was given.");
                }

            } else {

                Log.warning("Unable to write to "+this.destinationDirectory.toString()+".Please check directory permissions.");
            }

        } else {

            Log.info("File "+filename+" do not need to update");
        }
    }

    /**
     * Check time interval to update
     * @param fileDrivePath  file path
     * @return true if file need to be update
     */
    public  boolean isNeedUpdateFile(Path fileDrivePath) {

        try {

            BasicFileAttributes fileAttrs = Files.readAttributes(fileDrivePath, BasicFileAttributes.class);

            long lastAccessTime = fileAttrs.lastModifiedTime().toMillis();
            long currentTime = this.getCurrentTime();

            return (lastAccessTime+this.updateTime < currentTime);

        } catch (IOException e) {

            Log.info("File "+fileDrivePath.toString()+" was not found.Try to download.");
        }

        return true;

    }

    /**
     * Get Path Object from string
     * @param url source url
     * @return Path Object
     */
    protected Path getPath(String url) {

        return Paths.get(url);
    }

    /**
     * Get filename from url
     * @param url source url
     * @return filename
     */
    protected String getFilename(String url) {

        return url.substring(url.lastIndexOf("/")+1);
    }

    /**
     * get current unix timestamp
     * @return unix timestamp,ms
     */
    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }

    /**
     * Check if path writable
     * @param directoryPath source path
     * @return true if path writable
     */
    protected boolean checkPathWritable(Path directoryPath) {

        return (Files.exists(directoryPath) && Files.isWritable(directoryPath));
    }

    /**
     * Copy file
     * @param input source stream
     * @param target target path
     * @return true if copy is done
     */
    protected boolean copyFile(InputStream input,Path target) {

        try {

            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);

            Log.info("File "+target.toString()+" has been saved.");

            return true;

        } catch (IOException e) {

            Log.warning("Unable to save "+target.toString()+".Check directory permission.");
        }

        return false;
    }


}
