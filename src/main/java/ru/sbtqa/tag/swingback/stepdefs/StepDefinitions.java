package ru.sbtqa.tag.swingback.stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.swingback.AppManager;
import ru.sbtqa.tag.swingback.TestContext;
import ru.sbtqa.tag.swingback.exceptions.FormInitializationException;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StepDefinitions {

    /**
     * Start the application.
     */
    @And("^user open the application$")
    @И("(?:пользователь |он |)открывает приложение$")
    public void startApp() throws NoSuchMethodException {
        AppManager.getInstance().startApplication();
    }

    /**
     * Closing of application
     */
    @And("^user close the application$")
    @И("(?:пользователь |он |)закрывает приложение$")
    public void closeApp() {
        AppManager.getInstance().stopApplication();
    }

    @And("^opening form \"(.*?)\"$")
    @И("^открывается форма \"(.*?)\"$")
    public void openForm(String title) throws FormInitializationException {
        AppManager.getInstance().execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                TestContext.setForm(title);
                return null;
            }
        });
    }

    /**
     * Switch current context from the container (Frame, Dialog) to page of the tabbed pane with specified title and pane.
     * @param pane pane name
     * @param title tabbed pane title
     */
    @And("^user select the tab \"(.*?)\" on the tabbed pane \"(.*?)\"$")
    @И("^(?:пользователь |он |)переходит на вкладку \"(.*?)\" в окне с вкалдками \"(.*?)\"$")
    public void selectTabe(String pane, String title) throws FormInitializationException {
        AppManager.getInstance().execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                TestContext.getCurrentForm().selectTabPane(title, pane);
                return null;
            }
        });
    }

    /**
     * Switch context back to the container
     */
    @And("^user working with form$")
    @И("^(?:пользователь |он |)работает с формой$")
    public void workingWithForm() {
        AppManager.getInstance().execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                TestContext.getCurrentForm().switchToContainer();
                return null;
            }
        });
    }

    /**
     * waining in seconds for a specified time
     * @param wait
     * @throws InterruptedException
     */
    @And("^wait (.*?) sec$")
    @И("^ожидание (.*?) сек$")
    public void wait(String wait) throws InterruptedException {
        SECONDS.sleep(Long.parseLong(wait));
    }

    /**
     * Execute action with no parameters
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @And("^(?:user |)\\((.*?)\\)$")
    @И("^(?:пользователь |он)\\((.*?)\\)$")
    public void userActionNoParams(String action) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action);
                return null;
            }
        });
    }

    /**
     * Execute action with one parameter
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @And("^(?:user |)\\((.*?)\\) (?:with param |)\"([^\"]*)\"$")
    @И("^(?:пользователь |он)\\((.*?)\\) (?:с параметром |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action, param);
                return null;
            }
        });
    }

    /**
     * Execute action with two parameters
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\"$")
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрарми |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action, param1, param2);
                return null;
            }
        });
    }

    /**
     * Execute action with three parameters
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @param param3 third parameter
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрарми |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action, param1, param2, param3);
                return null;
            }
        });
    }

    /**
     * Execute action with parameters from given {@link cucumber.api.DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @And("^user \\((.*?)\\) data$")
    @И("^(?:пользователь |он |)\\((.*?)\\) данными$")
    public void userActionTableParam(String action, DataTable dataTable) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action, dataTable);
                return null;
            }
        });
    }

    /**
     * Execute action with string parameter and {@link cucumber.api.DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @param dataTable table of parameters
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @And("^user \\((.*?)\\) [^\"]*\"([^\"]*) data$")
    @И("^(?:пользователь |он |)\\((.*?)\\) [^\"]*\"([^\"]*)\" данными$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action, param, dataTable);
                return null;
            }
        });
    }

}
