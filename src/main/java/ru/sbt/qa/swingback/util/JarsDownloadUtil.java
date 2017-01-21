package ru.sbt.qa.swingback.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util for downloading the application jars to required folder.
 * Created by Varivoda Ivan on 21.01.2017.
 */
public class JarsDownloadUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JarsDownloadUtil.class);


    public static void downloadJarsByJnlp(String jarsFolderPath, String JnlpHref) throws IOException {
        LOG.info("Download jars...");
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
