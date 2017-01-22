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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manager for downloading the application jars to required folder and preparation of the necessary data.
 * Created by Varivoda Ivan on 21.01.2017.
 */
public class AppDownloadManager {

    private static final Props props = Props.getInstance();

    public static final String PROP_NAME_APP_JARS_PATH_ABS = "swingback.app.jars.path.abs";
    public static final String PROP_NAME_APP_JARS_PATH_REL = "swingback.app.jars.path.rel";
    public static final String PROP_NAME_JNLP_HREF = "swingback.app.jnlp";
    public static final String DEFAULT_APP_JARS_FOLDER = "app/jars";
    public static final String JVM_PROP_PREFIX = "swingback.jvm.prop.";
    public static final String PROP_NAME_START_CLASS = "swingback.app.startclass";


    public static String getStartClassName() {
        return props.get(PROP_NAME_START_CLASS);
    }

    public static Map<String, String> getSystemProperties() {
        return props.getProps()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().toString().startsWith(JVM_PROP_PREFIX))
                .collect(Collectors.toMap(o -> o.getKey().toString(), o -> o.getValue().toString()));
    }

    enum DownloadType {
        JNLP
    }

    /**
     * Return a folder path where will be stored the application jars.
     *
     * @throws FileNotFoundException If folder is not founded.
     * @author Varivoda Ivan
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
            jarsFolderPath = getJarsFolder();
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
