package ru.sbt.qa.swingback;

import ru.sbt.qa.swingback.util.JarsDownloadUtil;
import ru.sbtqa.tag.qautils.properties.Props;

import java.io.File;
import java.io.IOException;
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

    enum DownloadType {
        JNLP
    }


    public static final String DEFAULT_APP_JARS_FOLDER = "app/jars";

    /**
     * Return a folder path where will be stored the application jars.
     *
     * @author Varivoda Ivan
     */
    public static String getRequiredAppJarsFolder() {
        //check abs path from properties
        String absPath = props.get(PROP_NAME_APP_JARS_PATH_ABS);
        if (!absPath.isEmpty()) {
            return absPath;
        }
        // check custom rel path from properties
        String custRelPath = props.get(PROP_NAME_APP_JARS_PATH_REL);
        if (!custRelPath.isEmpty()) {
            return JarsDownloadUtil.class.getClassLoader().getResource(custRelPath).getPath();
        }
        return JarsDownloadUtil.class.getClassLoader().getResource(DEFAULT_APP_JARS_FOLDER).getPath();
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
    public static String setEnvironment() {
        String jarsFolder = getRequiredAppJarsFolder();
        Path jarsPath = Paths.get(jarsFolder);
        if (jarsFolder.isEmpty() || !Files.exists(jarsPath)) {
            throw new UnsupportedOperationException("Required folder isn't exist. Check the properties file.");
        }

        // downloading jars if folder isn't empty
        File[] files = jarsPath.toFile().listFiles();
        if (files.length == 0) {
            try {
                downloadAppJars(jarsFolder, DownloadType.JNLP);
            } catch (IOException e) {
                //todo
                throw new RuntimeException();
            }
        }
        return jarsFolder;
    }
}
