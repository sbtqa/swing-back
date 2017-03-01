package ru.sbtqa.tag.swingback.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.qautils.properties.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manager for downloading the application jars to required folder and preparation of the necessary data.
 */
public class FileSystemAppDownloadManager extends AppDownloadManager {

    private final Logger log = LoggerFactory.getLogger(FileSystemAppDownloadManager.class);

    public static final String JVM_PROP_PREFIX = "swingback.jvm.prop.";
    public static final String PROP_NAME_START_CLASS = "swingback.app.startclass";

    @Override
    public String getStartClassName() {
        return Props.get(PROP_NAME_START_CLASS);
    }

    @Override
    public Map<String, String> getSystemProperties() {
        return Props.getProps()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().toString().startsWith(JVM_PROP_PREFIX))
                .collect(Collectors.toMap(o -> o.getKey().toString().replace(JVM_PROP_PREFIX, ""), o -> o.getValue().toString()));
    }

    @Override
    public URL[] getJarsURL() {
        File jarsFolder;
        try {
            jarsFolder = new File(getJarsFolder());
        } catch (FileNotFoundException e) {
            throw new ApplicationDownloadException("Specified folder isn't exist. Check the properties file.", e);
        }

        if (jarsFolder.listFiles() != null && jarsFolder.listFiles().length == 0) {
            throw new ApplicationDownloadException("Specified folder is empty.");
        }
        return Stream.of(jarsFolder.list())
                .filter(name -> name.endsWith("jar"))
                .map(name -> new File(jarsFolder.getAbsolutePath() + "/" + name).toURI())
                .map(uri -> {
                    URL u = null;
                    try {
                        u = uri.toURL();
                    } catch (MalformedURLException ex) {
                        throw new ApplicationDownloadException("An Exception occurred while trying the string uri to URL.", ex);
                    }
                    return u;
                })
                .toArray(URL[]::new);
    }

    @Override
    public URL getResourcesURL() {
        try {
            return new File(getResourcesFolder()).toURI().toURL();
        } catch (FileNotFoundException e) {
            log.info("Resources directory is not specified.", e);
            return null;
        } catch (MalformedURLException e) {
            throw new ApplicationDownloadException("An Exception occurred while trying the string uri to URL.", e);
        }
    }

}