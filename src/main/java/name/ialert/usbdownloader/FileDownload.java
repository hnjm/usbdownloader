package name.ialert.usbdownloader;


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

    protected String filePath;

    protected Path destinationDirectory;

    protected long updateTime;

    public FileDownload(String fileUrl,String destinationDirectory,long updateTime) {

        this.filePath = fileUrl;
        this.destinationDirectory = this.getPath(destinationDirectory);
        this.updateTime = updateTime * 1000;
    }

    protected Path getPath(String url) {

        return Paths.get(url);
    }

    protected String getFilename(String url) {

        return url.substring(url.lastIndexOf("/")+1);
    }

    public void downloadFile() {

        String filename = this.getFilename(this.filePath);
        Path target = this.destinationDirectory.resolve(filename.toString());

        if(this.isNeedUpdateFile(target)) {

            if (this.checkPathWritable(this.destinationDirectory)) {

                URL downloadUrl;

                try {

                    Logger.add("Start downloading "+this.filePath);

                    downloadUrl = new URL(this.filePath);

                    try (InputStream in = downloadUrl.openStream()) {

                        Logger.add("Download finished.");

                        this.copyFile(in, target);

                        in.close();

                    } catch (IOException e) {

                        Logger.add("Unable to download file "+this.filePath+"."+e.getMessage());
                    }

                } catch (MalformedURLException e) {

                    Logger.add("Incorrect url was given.");
                }

            }

        } else {

            Logger.add("File "+filename+" do not need to update");
        }
    }

    public  boolean isNeedUpdateFile(Path fileDrivePath) {

        try {

            BasicFileAttributes fileAttrs = Files.readAttributes(fileDrivePath, BasicFileAttributes.class);

            long lastAccessTime = fileAttrs.lastModifiedTime().toMillis();
            long currentTime = this.getCurrentTime();

            return (lastAccessTime+this.updateTime < currentTime);

        } catch (IOException e) {

            Logger.add("File "+fileDrivePath.toString()+" was not found.Try to download.");
        }

        return true;

    }

    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }

    protected boolean checkPathWritable(Path directoryPath) {

        return (Files.exists(directoryPath) && Files.isWritable(directoryPath));
    }

    protected boolean copyFile(InputStream input,Path target) {

        try {

            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);

            return true;

        } catch (IOException e) {

            Logger.add("Unable to save "+target.toString()+".Check directory permission.");
        }

        return false;
    }

    public Void call() {

        this.downloadFile();

        return null;
    }
}
