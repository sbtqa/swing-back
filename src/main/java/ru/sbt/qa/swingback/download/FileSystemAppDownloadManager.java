package ru.sbt.qa.swingback.download;

import ru.sbt.qa.swingback.AppManagerException;
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


    public static final String JVM_PROP_PREFIX = "swingback.jvm.prop.";
    public static final String PROP_NAME_START_CLASS = "swingback.app.startclass";

    public FileSystemAppDownloadManager() {
        props = Props.getInstance();
    }

    @Override
    public String getStartClassName() {
        return props.get(PROP_NAME_START_CLASS);
    }

    @Override
    public Map<String, String> getSystemProperties() {
        return props.getProps()
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
            throw new ApplicationDownloadException("Specified folder isn't exist. Check the properties file.");
        }

        if ( jarsFolder.listFiles() != null && jarsFolder.listFiles().length == 0) {
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
                        throw new AppManagerException("An Exception occurred while trying the string uri to URL.", ex);
                    }
                    return u;
                })
                .toArray(URL[]::new);
    }

    @Override
    public boolean configIsValid() {
        return true;
    }
}