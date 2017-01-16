package ru.sbt.qa.swingback.util;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Manage file downloads
 *
 * @author sbt-sidochenko-vv
 */
public class DownloadManager {

    private URL uri;

    public DownloadManager(String url) throws MalformedURLException {
        this.uri = new URL(url);
    }

    /**
     * Download file to destination on file system
     *
     * @param destination path to file
     * @throws IOException
     */
    public void download(String destination) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] downloadContend = IOUtils.toByteArray(this.getUri());
            fos.write(downloadContend);
        }
    }

    /**
     * Download file to byte array
     *
     * @return downloadContent
     * @throws IOException
     */
    public byte[] download() throws IOException {
        byte[] downloadContent = IOUtils.toByteArray(this.getUri());
        return downloadContent;

    }

    /**
     * @return the uri
     */
    public URL getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(URL uri) {
        this.uri = uri;
    }

}
