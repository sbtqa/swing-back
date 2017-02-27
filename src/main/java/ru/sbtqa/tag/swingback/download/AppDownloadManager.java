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
    public static final String PROP_NAME_APP_RESOURCES_PATH_ABS = "swingback.app.resources.path.abs";
    public static final String DEFAULT_APP_JARS_FOLDER = "app/jars";
    public static final String DEFAULT_APP_RESOURCES_FOLDER = "app/resources";

    public static Props props = Props.getInstance();

    /**
     * Return a folder path where the application jars are stored.
     *
     * @throws FileNotFoundException If folder is not founded.
     */
    public static String getJarsFolder() throws FileNotFoundException {
        return getFolderPath(PROP_NAME_APP_JARS_PATH_ABS, DEFAULT_APP_JARS_FOLDER);
    }

    /**
     * Return a folder path where the application resources are stored.
     *
     * @throws FileNotFoundException If folder is not founded.
     */
    public static String getResourcesFolder() throws FileNotFoundException {
        return getFolderPath(PROP_NAME_APP_RESOURCES_PATH_ABS, DEFAULT_APP_RESOURCES_FOLDER);
    }

    /**
     * Return path to folder on the basis of prioritization.
     * The firsts - abs path. Second - default path
     *
     * @throws FileNotFoundException
     */
    private static String getFolderPath(String absFolderPropName, String defaultPath) throws FileNotFoundException {
        //check abs path from properties
        String absPath = props.get(absFolderPropName);
        if (!absPath.isEmpty()) {
            if (Files.exists(Paths.get(absPath))) {
                return absPath;
            }
        }
        URL resource = FileSystemAppDownloadManager.class.getClassLoader().getResource(defaultPath);
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

    /**
     * @return URLs of the application resources. Null if it does not exist
     */
    public abstract URL getResourcesURL();

}
