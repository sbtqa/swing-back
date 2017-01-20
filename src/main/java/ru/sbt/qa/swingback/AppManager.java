package ru.sbt.qa.swingback;

import org.apache.commons.io.FileUtils;
import org.gridkit.nanocloud.Cloud;
import org.gridkit.nanocloud.CloudFactory;
import org.gridkit.nanocloud.VX;
import org.gridkit.vicluster.ViNode;
import org.netbeans.jemmy.ClassReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbt.qa.swingback.util.DownloadManager;
import ru.sbtqa.tag.qautils.properties.Props;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author sbt-varivoda-ia
 * @date 16.01.2017
 */
public class AppManager {

    private static AppManager instance;
    private static Props props = Props.getInstance();

    private final static Logger LOG = LoggerFactory.getLogger(AppManager.class);

    private static String JVM_PROP_PREFIX = "swingback.jvm.prop.";
    private static String JARS_FOLDER = props.get("swingback.app.jars.folder");
    private static String START_CLASS = props.get("swingback.app.startclass");
    private static String JNLP_HREF = props.get("swingback.app.jnlp");

    private Cloud cloud;
    private ViNode allNodes;

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

    // Загружает в указанную дир jars
    private void downloadJars() throws IOException {
        File[] listFiles = new File(JARS_FOLDER).listFiles();
        if (listFiles == null || listFiles.length == 0) // download jars if jars folder is empty
        {
            try {
                LOG.info("Download jars...");
                final String bankUrl = JNLP_HREF.substring(0, JNLP_HREF.lastIndexOf('/') + 1);
                DownloadManager dm = new DownloadManager(JNLP_HREF);
                byte[] file = dm.download();

                String context = new String(file, StandardCharsets.UTF_8);
                Matcher matcher = Pattern.compile("jar\\s*(?i)href\\s*=\\s*\"(([^\"]*\")|'[^']*'|([^'\">\\s]+))\"").
                        matcher(context);
                while (matcher.find()) {
                    dm = new DownloadManager(bankUrl + matcher.group(1));
                    file = dm.download();
                    FileUtils.writeByteArrayToFile(new File(JARS_FOLDER + "/" + matcher.group(1)), file);
                }
            } catch (IOException ex) {
                throw new IOException("An Exception occurred while downloading jars.", ex);
            }
        }
    }

    public void startApplication() {
        new Thread(() -> execute((Serializable & Callable<Void>) () -> {

            // Setting jvm params
            props.getProps()
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().toString().startsWith(JVM_PROP_PREFIX))
                    .forEach(e -> System.setProperty(e.getKey().toString().replace(JVM_PROP_PREFIX, ""), e.getValue().toString()));

            // loading application jars from folder
            File folder = new File(JARS_FOLDER);
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
            Class<?> mainClass = loader.loadClass(START_CLASS);
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
