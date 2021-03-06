package ru.sbtqa.tag.swingback;

import org.gridkit.nanocloud.Cloud;
import org.gridkit.nanocloud.CloudFactory;
import org.gridkit.nanocloud.VX;
import org.gridkit.vicluster.ViNode;
import org.netbeans.jemmy.ClassReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.swingback.download.AppDownloadManager;
import ru.sbtqa.tag.swingback.download.FileSystemAppDownloadManager;
import ru.sbtqa.tag.swingback.exceptions.AppManagerException;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Singleton for managing the test application.
 */
public class AppManager {

    private static AppManager instance;

    private static final Logger LOG = LoggerFactory.getLogger(AppManager.class);

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
    
    public static void clearAppManager(){
        instance = null;
    }

    /**
     * Method run the application which is in filesystem
     */
    public void startApplication() {
        startApplication(new FileSystemAppDownloadManager());
    }

    /**
     * run the testing application with required download strategy
     *
     * @param downloadManager strategy
     */
    public void startApplication(AppDownloadManager downloadManager) {
        URL[] jarsURL = downloadManager.getJarsURL();
        URL resourcesURL = downloadManager.getResourcesURL();
        URL[] appJarsAndResourcesURL;
        if (resourcesURL != null) {
            appJarsAndResourcesURL = Arrays.copyOf(jarsURL, jarsURL.length + 1);
            appJarsAndResourcesURL[appJarsAndResourcesURL.length - 1] = resourcesURL;
        } else {
            appJarsAndResourcesURL = jarsURL;
        }

        startApplication(appJarsAndResourcesURL, downloadManager.getStartClassName(), downloadManager.getSystemProperties());
    }

    /**
     * Run application
     *
     * @param appJarsAndResources URLs to the required testing application jars
     * @param startClassName      full start class name
     * @param sysProps            required system environment properties for testing application
     */
    public void startApplication(final URL[] appJarsAndResources, final String startClassName, final Map<String, String> sysProps) {
        // debug run
        String jarsString = null;
        if (LOG.isDebugEnabled()) {
            jarsString = Arrays.toString(appJarsAndResources);
        }
        LOG.debug("Application starts with params: " +
                "\nURLS: %s " +
                "\nStart class name: %s" +
                "\nSystem properties: %s", jarsString, startClassName, sysProps);

        execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // Setting jvm params
                Properties properties = System.getProperties();
                properties.putAll(sysProps);
                System.setProperties(properties);
                // loading application by urls
                URLClassLoader loader = new URLClassLoader(appJarsAndResources, System.class.getClassLoader());
                // run application
                Class<?> mainClass;
                try {
                    mainClass = loader.loadClass(startClassName);
                    Object app = mainClass.newInstance();
                    Thread.currentThread().setContextClassLoader(loader);
                    new ClassReference(app).startApplication();
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new AppManagerException("An error occurred while starting application.", e);
                }
                return null;
            }
        });
    }

    /**
     * stop application
     */
    public void stopApplication() {
        allNodes.kill();
        cloud.shutdown();
    }

    public <T> T execute(Callable<T> task) {
        return allNodes.exec(new Callable<T>() {
            @Override
            public T call() throws Exception {
                // gridkit require anonymous class for serialization
                return task.call();
            }
        });
    }

}
