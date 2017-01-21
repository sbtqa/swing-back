package ru.sbt.qa.swingback;

import ru.sbt.qa.swingback.util.JarsDownloadUtil;
import ru.sbtqa.tag.qautils.properties.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manager for downloading the application jars to required folder.
 * Created by Varivoda Ivan on 21.01.2017.
 */
public class AppDownloadManager {

    private static Props props = Props.getInstance();

    public static final String PROP_NAME_APP_JARS_PATH_ABS = "swingback.app.jars.path.abs";
    public static final String PROP_NAME_APP_JARS_PATH_REL = "swingback.app.jars.path.rel";
    public static final String PROP_NAME_JNLP_HREF = "swingback.app.jnlp";
    public static final String DEFAULT_APP_JARS_FOLDER = "app/jars";

    enum DownloadType {
        JNLP
    }

    /**
     * Return a folder path where will be stored the application jars.
     *
     * @throws FileNotFoundException If folder is not founded.
     * @author Varivoda Ivan
     */
    public static String getRequiredAppJarsFolder() throws FileNotFoundException {
        //check abs path from properties
        String absPath = props.get(PROP_NAME_APP_JARS_PATH_ABS);
        if (!absPath.isEmpty()) {
            if (Files.exists(Paths.get(absPath))) {
                return absPath;
            }
        }
        // check custom rel path from properties
        String custRelPath = props.get(PROP_NAME_APP_JARS_PATH_REL);
        if (!custRelPath.isEmpty()) {
            URL resource = AppDownloadManager.class.getClassLoader().getResource(custRelPath);
            if (resource != null)
                return new File(resource.getPath()).getAbsolutePath();
        }
        URL resource = AppDownloadManager.class.getClassLoader().getResource(DEFAULT_APP_JARS_FOLDER);
        if (resource != null) {
            return new File(resource.getPath()).getAbsolutePath();
        }
        throw new FileNotFoundException();
    }

    /**
     * Downloading application jars to the specified folder by the type.
     *
     * @autor Varivoda Ivan
     */
    public static void downloadAppJars(String jarsFolderPath, DownloadType downloadType) throws IOException {
        switch (downloadType) {
            case JNLP:
                String jnlpHref = props.get(PROP_NAME_JNLP_HREF);
                JarsDownloadUtil.downloadJarsByJnlp(jarsFolderPath, jnlpHref);
                break;
            default:
                throw new UnsupportedOperationException("Downloading application jars with type " + downloadType + " is not supported.");
        }
    }

    /**
     * Getting folder and downloading application jars.
     *
     * @autor Varivoda Ivan
     */
    public static String downloadJarsAndGetPath() {
        String jarsFolderPath;
        try {
            jarsFolderPath = getRequiredAppJarsFolder();
        } catch (FileNotFoundException e) {
            throw new ApplicationDownloadException("Required folder isn't exist. Check the properties file.");
        }
        // downloading jars if folder isn't empty
        Path jarsPath = Paths.get(jarsFolderPath);
        File[] files = jarsPath.toFile().listFiles();
        if (files.length == 0) {
            try {
                downloadAppJars(jarsFolderPath, DownloadType.JNLP);
            } catch (IOException e) {
                throw new ApplicationDownloadException("An exception was occurred while downloading the application jars.", e);
            }
        }
        return jarsFolderPath;
    }
}
