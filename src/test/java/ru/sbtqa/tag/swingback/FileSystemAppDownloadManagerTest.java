package ru.sbtqa.tag.swingback;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.sbtqa.tag.swingback.download.FileSystemAppDownloadManager;
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
    private static String defaultResourcesFolder = defaultJarsAppFolder + File.separator + "resources";

    private static String customRelativeJarsParentFolder = "target" + File.separator + "test-classes" + File.separator + "app1";
    private static String customRelativeJarsFolder = customRelativeJarsParentFolder + File.separator + "jars";
    private static String customRelativeJarsFolderProperty = "app1/jars";

    private static String abstJarsFolder = new File("abs").getAbsolutePath();
    private static String absResourcesFolder = new File("res").getAbsolutePath();

    private FileSystemAppDownloadManager fileSystemAppDownloadManager;

    @Before
    public void setUp() throws IOException {
        PowerMockito.mockStatic(Props.class);
        fileSystemAppDownloadManager = new FileSystemAppDownloadManager();
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
    public void tearDown() throws IOException {
        deleteIfExist(propFilePath);
        deleteIfExist(configPath);
        deleteIfExist(defaultJarsFolder);
        deleteIfExist(defaultResourcesFolder);
        deleteIfExist(defaultJarsAppFolder);
        deleteIfExist(abstJarsFolder);
        deleteIfExist(absResourcesFolder);
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
        Files.createDirectories(Paths.get(defaultJarsFolder));
        Assert.assertEquals(new File(defaultJarsFolder).getAbsolutePath(), fileSystemAppDownloadManager.getJarsFolder());
    }

    @Test
    public void getAbsoluteAppJarsPathFolderWhenPropertyFileContainsCorrectAbsPath() throws IOException, InterruptedException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn(abstJarsFolder);
        createDirIfNotExist(abstJarsFolder);
        assertEquals(abstJarsFolder, fileSystemAppDownloadManager.getJarsFolder());
    }

    @Test(expected = FileNotFoundException.class)
    public void throwFileNotFoundedExcWhenAbsDirPathIsNotExist() throws IOException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn(abstJarsFolder);
        fileSystemAppDownloadManager.getJarsFolder();
    }

    @Test(expected = FileNotFoundException.class)
    public void throwFileNotFoundedExceptionIfDefaultFolderIsNotExist() throws IOException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_JARS_PATH_ABS)).thenReturn("");
        fileSystemAppDownloadManager.getJarsFolder();
    }

    @Test
    public void getDefaultAppResourceFolderPathWhenPropertiesIsNotContainsAbsFolderProp() throws Exception {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_RESOURCES_PATH_ABS)).thenReturn("");
        Files.createDirectories(Paths.get(defaultResourcesFolder));
        Assert.assertEquals(new File(defaultResourcesFolder).getAbsolutePath(), fileSystemAppDownloadManager.getResourcesFolder());
    }

    @Test
    public void getAbsoluteAppResourcesPathFolderWhenPropertyFileContainsCorrectAbsPath() throws IOException, InterruptedException {
        PowerMockito.when(Props.get(FileSystemAppDownloadManager.PROP_NAME_APP_RESOURCES_PATH_ABS)).thenReturn(absResourcesFolder);
        createDirIfNotExist(absResourcesFolder);
        assertEquals(absResourcesFolder, fileSystemAppDownloadManager.getResourcesFolder());
    }

    @Test
    public void returnEmptySysPropsWhenPropsFileDoesNotContainsAnySysProp() {
        Properties p = new Properties();
        p.put("Prop1", "val1");
        p.put("prop2", "val2");
        PowerMockito.when(Props.getProps()).thenReturn(p);
        assertThat(fileSystemAppDownloadManager.getSystemProperties().isEmpty(), is(true));
    }

    @Test
    public void returnSysPropsWithOnePropWhenPropsFileContainsOneSysProp() {
        Properties p = new Properties();
        p.put("Prop1", "val1");
        p.put("prop2", "val2");
        String expPropName = "prop3";
        String expPropVal = "val3";
        p.put(FileSystemAppDownloadManager.JVM_PROP_PREFIX + expPropName, expPropVal);
        PowerMockito.when(Props.getProps()).thenReturn(p);
        assertThat(fileSystemAppDownloadManager.getSystemProperties().size(), is(1));
        assertThat(fileSystemAppDownloadManager.getSystemProperties().isEmpty(), is(false));
        assertThat(fileSystemAppDownloadManager.getSystemProperties().get(expPropName), is(expPropVal));
    }
}
