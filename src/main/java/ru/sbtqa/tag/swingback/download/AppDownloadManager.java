package ru.sbtqa.tag.swingback.download;

import ru.sbtqa.tag.qautils.properties.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Basic methods for downloading application and obtaining necessary information about it.
 */
public abstract class AppDownloadManager {

    public static final String PROP_NAME_APP_JARS_PATH_ABS = "swingback.app.jars.path.abs";
    public static final String PROP_NAME_APP_JARS_PATH_REL = "swingback.app.jars.path.rel";
    public static final String DEFAULT_APP_JARS_FOLDER = "app/jars";
    public static Props props = Props.getInstance();

    /**
     * Return a folder path where will be stored the application jars.
     *
     * @throws FileNotFoundException If folder is not founded.
     */
    public static String getJarsFolder() throws FileNotFoundException {
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
            URL resource = FileSystemAppDownloadManager.class.getClassLoader().getResource(custRelPath);
            if (resource != null)
                return new File(resource.getPath()).getAbsolutePath();
        }
        URL resource = FileSystemAppDownloadManager.class.getClassLoader().getResource(DEFAULT_APP_JARS_FOLDER);
        if (resource != null) {
            return new File(resource.getPath()).getAbsolutePath();
        }
        throw new FileNotFoundException();
    }

    /**
     * @return the start class name of the application which will be started
     */
    public abstract String getStartClassName();

    /**
     * @return system properties which will be set in the jvm context
     */
    public abstract Map<String, String> getSystemProperties();

    /**
     * @return URLs of the application jars
     */
    public abstract URL[] getJarsURL();

}
