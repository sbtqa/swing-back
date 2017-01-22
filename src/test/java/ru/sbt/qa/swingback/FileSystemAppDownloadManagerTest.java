package ru.sbt.qa.swingback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.sbt.qa.swingback.download.AppDownloadManager;
import ru.sbt.qa.swingback.download.FileSystemAppDownloadManager;
import ru.sbtqa.tag.qautils.properties.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Props.class})
public class FileSystemAppDownloadManagerTest {

    private static String configPath = "target" + File.separator + "test-classes" + File.separator + "config";
    private static String propFilePath = configPath + File.separator + "application.properties";

    private static String defaultJarsAppFolder = "target" + File.separator + "test-classes" + File.separator + "app";
    private static String defaultJarsFolder = defaultJarsAppFolder + File.separator + "jars";

    private static String customRelativeJarsParentFolder = "target" + File.separator + "test-classes" + File.separator + "app1";
    private static String customRelativeJarsFolder = customRelativeJarsParentFolder + File.separator + "jars";
    private static String customRelativeJarsFolderProperty = "app1/jars";

    private static String abstJarsFolder = new File("abs").getAbsolutePath();

    private FileSystemAppDownloadManager downloadManager;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Props.class);
        downloadManager = new FileSystemAppDownloadManager();
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


    @Test
    public void getDefaultAppJarsFolderIfPropertyFileHasNotSpecifiedCustomFolder() throws IOException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");

        Files.createDirectories(Paths.get(defaultJarsFolder));
        assertEquals(new File(defaultJarsFolder).getAbsolutePath(), AppDownloadManager.getJarsFolder());
    }

    @Test
    public void getAbsoluteAppJarsPathFolderIfPropertyFileContainsCorrectAbsPathInProperty() throws IOException, InterruptedException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn(abstJarsFolder);
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");

        createDirIfNotExist(abstJarsFolder);
        assertEquals(abstJarsFolder, downloadManager.getJarsFolder());
    }

    @Test(expected = FileNotFoundException.class)
    public void throwFileNotFoundedExcWhenAbsDirPathIsNotExist() throws IOException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn(abstJarsFolder);
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");
        FileSystemAppDownloadManager.getJarsFolder();
    }

    @Test(expected = FileNotFoundException.class)
    public void throwFileNotFoundedExceptionIfDefaultFolderIsNotExist() throws IOException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn("");
        FileSystemAppDownloadManager.getJarsFolder();
    }

    @Test
    public void getAbsoluteJarsFolderPathFromCorrectCustomRelativeFolderPathCorrectSpecifiedInPropertyFile() throws Exception {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_REL)).thenReturn(customRelativeJarsFolderProperty);
        createDirsIfNotExist(customRelativeJarsFolder);
        assertEquals(new File(customRelativeJarsFolder).getAbsolutePath(), FileSystemAppDownloadManager.getJarsFolder());
    }

    @Test
    public void returnEmptySysPropsWhenPropsFileDoesNotContainsAnySysProp() {
        Properties p = new Properties();
        p.put("Prop1", "val1");
        p.put("prop2", "val2");
        PowerMockito.when(Props.getProps()).thenReturn(p);
        assertThat(downloadManager.getSystemProperties().isEmpty(), is(true));
    }

    @Test
    public void returnSysPropsWithOnePropWhenPropsFileContainsOneSysProp() {
        Properties p = new Properties();
        p.put("Prop1", "val1");
        p.put("prop2", "val2");
        p.put(FileSystemAppDownloadManager.JVM_PROP_PREFIX + "prop3", "val3");
        PowerMockito.when(Props.getProps()).thenReturn(p);
        assertThat(downloadManager.getSystemProperties().size(), is(1));
        assertThat(downloadManager.getSystemProperties().isEmpty(), is(false));
    }
}
