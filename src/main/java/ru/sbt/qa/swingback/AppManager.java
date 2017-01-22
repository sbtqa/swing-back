package ru.sbt.qa.swingback;

import org.gridkit.nanocloud.Cloud;
import org.gridkit.nanocloud.CloudFactory;
import org.gridkit.nanocloud.VX;
import org.gridkit.vicluster.ViNode;
import org.netbeans.jemmy.ClassReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author sbt-varivoda-ia
 * @date 16.01.2017
 */
public class AppManager {

    private final static Logger LOG = LoggerFactory.getLogger(AppManager.class);

    private static AppManager instance;

    private final Cloud cloud;
    private final ViNode allNodes;

    private AppManager() {
        cloud = CloudFactory.createCloud();
        cloud.node("**").x(VX.TYPE).setLocal();
        cloud.node("node1");
        allNodes = cloud.node("**");
    }

    public static synchronized AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void startApplication() {
        String jarsFolder;
        try {
            jarsFolder = AppDownloadManager.getJarsFolder();
        } catch (FileNotFoundException e) {
            throw new AppManagerException("Required folder isn't exist. Check the properties file.", e);
        }

        String startClassName = AppDownloadManager.getStartClassName();
        Map<String, String> sysProps = AppDownloadManager.getSystemProperties();

        startApplication(jarsFolder, startClassName, sysProps);
    }

    private void startApplication(final String jarsFolder, final String startClassName, Map<String, String> sysProps) {
        new Thread(() -> execute(() -> {

            // Setting jvm params
            Properties properties = System.getProperties();
            properties.putAll(sysProps);
            System.setProperties(properties);

            // loading application jars from folder
            File folder = new File(jarsFolder);
            URL[] appJars = Stream.of(folder.list())
                    .filter(name -> name.endsWith("jar"))
                    .map(name -> new File(folder.getAbsolutePath() + "/" + name).toURI())
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

            URLClassLoader loader = new URLClassLoader(appJars, System.class.getClassLoader());

            // run application
            Class<?> mainClass = loader.loadClass(startClassName);
            Object app = mainClass.newInstance();
            Thread.currentThread().setContextClassLoader(loader);

            LOG.info("Start loading main class.");
            new ClassReference(app).startApplication();
            return null;
        })).start();
    }

    public void stopApplication() {
        allNodes.kill();
        cloud.shutdown();
    }

    public <T> T execute(Callable<T> task) {
        return allNodes.exec(task);
    }

}
