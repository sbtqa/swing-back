package ru.sbt.qa.swingback;

import org.gridkit.nanocloud.Cloud;
import org.gridkit.nanocloud.CloudFactory;
import org.gridkit.nanocloud.VX;
import org.gridkit.vicluster.ViNode;
import org.netbeans.jemmy.ClassReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbt.qa.swingback.download.AppDownloadManager;
import ru.sbt.qa.swingback.download.FileSystemAppDownloadManager;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;

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
        startApplication(new FileSystemAppDownloadManager());
    }

    public void startApplication(AppDownloadManager downloadManager) {
        startApplication(downloadManager.getJarsURL(), downloadManager.getStartClassName(), downloadManager.getSystemProperties());
    }

    public void startApplication(final URL[] appJars, final String startClassName,final Map<String, String> sysProps) {
        new Thread(() -> execute(() -> {
            // Setting jvm params
            Properties properties = System.getProperties();
            properties.putAll(sysProps);
            System.setProperties(properties);
            // loading application by urls
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
