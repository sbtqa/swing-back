package ru.sbt.qa.swingback;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.sbtqa.tag.qautils.properties.Props;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Varivoda Ivan on 21.01.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Props.class})
public class AppDownloadManagerTest {

    private static String configPath = "target" + File.separator + "test-classes" + File.separator + "config";
    private static String propFilePath = configPath + File.separator + "application.properties";

    private static String defaultJarsAppFolder = "target" + File.separator + "test-classes" + File.separator + "app";
    private static String defaultJarsFolder = defaultJarsAppFolder + File.separator + "jars";

    private static String customRelativeJarsParentFolder = "target" + File.separator + "test-classes" + File.separator + "app1";
    private static String customRelativeJarsFolder = customRelativeJarsParentFolder + File.separator + "jars";
    private static String customRelativeJarsFolderProperty = "app1/jars";

    private static String abstJarsFolder = new File("abs").getAbsolutePath();

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Props.class);
        createDirIfNotExist(configPath);
        createFileIfNotExist(propFilePath);
    }

    private void createDirIfNotExist(String path) throws IOException {
        if (!Files.exists(Paths.get(path))) {
            Files.createDirectory(Paths.get(path));
        }
    }

    private static void createDirsIfNotExist(String path) throws IOException {
        if (!Files.exists(Paths.get(path))) {
            Files.createDirectories(Paths.get(path));
        }
    }

    private void createFileIfNotExist(String path) throws IOException {
        if (!Files.exists(Paths.get(path))) {
            Files.createFile(Paths.get(path));
        }
    }

    @After
    public void tearDown() throws Exception {
        deleteIfExist(propFilePath);
        deleteIfExist(configPath);
        deleteIfExist(defaultJarsFolder);
        deleteIfExist(defaultJarsAppFolder);
        deleteIfExist(abstJarsFolder);
        deleteIfExist(customRelativeJarsFolder);
        deleteIfExist(customRelativeJarsParentFolder);
    }

    private void deleteIfExist(String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            Files.delete(Paths.get(path));
        }
    }


    private static void addProp(String key, String value) throws IOException {
        Properties prop = new Properties();
        Path path = Paths.get(propFilePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            prop.load(br);
        }
        prop.put(key, value);
        try (Writer writer = Files.newBufferedWriter(path)) {
            prop.store(writer, "");
        }
    }



    @Test
    public void getDefaultAppJarsFolderIfPropertyFileHasNotSpecifiedCustomFolder() throws IOException {
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");

        Files.createDirectories(Paths.get(defaultJarsFolder));
        Assert.assertEquals(new File(defaultJarsFolder).getAbsolutePath(), AppDownloadManager.getRequiredAppJarsFolder());
    }

    @Test
    public void getAbsoluteAppJarsPathFolderIfPropertyFileContainsCorrectAbsPathInProperty() throws IOException, InterruptedException {
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn(abstJarsFolder);
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");

        createDirIfNotExist(abstJarsFolder);
        Assert.assertEquals(abstJarsFolder, AppDownloadManager.getRequiredAppJarsFolder());
    }

    @Test(expected = FileNotFoundException.class)
    public void throwFileNotFoundedExcWhenAbsDirPathIsNotExist() throws IOException {
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn(abstJarsFolder);
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");
        AppDownloadManager.getRequiredAppJarsFolder();
    }

    @Test(expected = FileNotFoundException.class)
    public void throwFileNotFoundedExceptionIfDefaultFolderIsNotExist() throws IOException {
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");
        addProp(AppDownloadManager.PROP_NAME_JNLP_HREF, "asdad");
        AppDownloadManager.getRequiredAppJarsFolder();
    }

    @Test
    public void getAbsoluteJarsFolderPathFromCorrectCustomRelativeFolderPathCorrectSpecifiedInPropertyFile() throws Exception {
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        PowerMockito.when(Props.get(AppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn(customRelativeJarsFolderProperty);
        createDirsIfNotExist(customRelativeJarsFolder);
        Assert.assertEquals(new File(customRelativeJarsFolder).getAbsolutePath(), AppDownloadManager.getRequiredAppJarsFolder());
    }
}
