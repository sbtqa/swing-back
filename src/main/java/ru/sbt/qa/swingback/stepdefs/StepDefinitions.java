package ru.sbt.qa.swingback.stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.И;
import ru.sbt.qa.swingback.AppManager;
import ru.sbt.qa.swingback.Callable;
import ru.sbt.qa.swingback.TestContext;
import ru.sbt.qa.swingback.jemmy.FormInitializationException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StepDefinitions {

    @И("(?:пользователь |он |)открывает приложение$")
    @And("^user open the application$")
    public void startApp() throws InterruptedException {
        AppManager.getInstance().startApplication();
        AppManager.getInstance().execute( () -> {
            TestContext.init();
            return null;
        });
    }

    @И("(?:пользователь |он |)закрывает приложение$")
    @And("^user close the application$")
    public void closeApp() throws InterruptedException {
        AppManager.getInstance().stopApplication();
    }

    @И("^открывается форма \"(.*?)\"$")
    @And("^opening form \"(.*?)\"$")
    public void m(String title) throws FormInitializationException {
        AppManager.getInstance().execute( () -> {
            TestContext.getForm(title);
            return null;
        });
    }

    @И("^wait (.*?)$")
    public void vait(String title) throws FormInitializationException, InterruptedException {
        SECONDS.sleep(Long.parseLong(title));
    }

    /**
     * Execute action with no parameters
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @И("^(?:пользователь |он |)\\((.*?)\\)$")
    @And("^user \\((.*?)\\)$")
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
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметром |)\"([^\"]*)\"$")
    @And("^user \\((.*?)\\) (?:with param |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws FormInitializationException, NoSuchMethodException {
        AppManager.getInstance().execute(() -> {
                TestContext.getCurrentForm().executeMethodByTitle(action, param);
                return null;
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
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрарми |)\"([^\"]*)\" \"([^\"]*)\"$")
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\"$")
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
     * @param param2 second patrameter
     * @param param3 third parameter
     * @throws FormInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрарми |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
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
    @И("^(?:пользователь |он |)\\((.*?)\\) данными$")
    @And("^user \\((.*?)\\) data$")
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
    @И("^(?:пользователь |он |)\\((.*?)\\) [^\"]*\"([^\"]*)\" данными$")
    @And("^user \\((.*?)\\) [^\"]*\"([^\"]*) data$")
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
