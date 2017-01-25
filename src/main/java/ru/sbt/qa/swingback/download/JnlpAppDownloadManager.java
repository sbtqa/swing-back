package ru.sbt.qa.swingback.download;

import org.apache.commons.io.FileUtils;
import ru.sbt.qa.swingback.util.DownloadManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JNLP download manager.
 */
public class JnlpAppDownloadManager extends AppDownloadManager {

    public static final String PROP_NAME_JNLP_HREF = "swingback.app.jnlp";

    @Override
    public String getStartClassName() {
        return null;
    }

    @Override
    public Map<String, String> getSystemProperties() {
        return null;
    }

    @Override
    public URL[] getJarsURL() {
        return new URL[0];
    }

    @Override
    public boolean configIsValid() {
        return false;
    }

    /**
     * Downloading application jars to the specified folder by the type.
     *
     * @autor Varivoda Ivan
     */
    public void downloadAppJars(String jarsFolderPath) throws IOException {
        String jnlpHref = props.get(PROP_NAME_JNLP_HREF);
        downloadJarsByJnlp(jarsFolderPath, jnlpHref);
    }

    public void downloadJarsByJnlp(String jarsFolderPath, String JnlpHref) throws IOException {
        final String appUrl = JnlpHref.substring(0, JnlpHref.lastIndexOf('/') + 1);
        DownloadManager dm = new DownloadManager(JnlpHref);
        byte[] file = dm.download();

        String context = new String(file, StandardCharsets.UTF_8);
        Matcher matcher = Pattern.compile("jar\\s*(?i)href\\s*=\\s*\"(([^\"]*\")|'[^']*'|([^'\">\\s]+))\"").
                matcher(context);
        while (matcher.find()) {
            dm = new DownloadManager(appUrl + matcher.group(1));
            file = dm.download();
            FileUtils.writeByteArrayToFile(new File(jarsFolderPath + File.separator + matcher.group(1)), file);
        }
    }

}
