package ru.sbtqa.tag.swingback.stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.И;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import ru.sbtqa.tag.swingback.AppManager;
import ru.sbtqa.tag.swingback.exceptions.SwingBackRuntimeException;
import ru.sbtqa.tag.swingback.TestContext;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StepDefinitions {

    /**
     * Start the application.
     */
    @And("openApp")
    public void startApp() {
        AppManager.getInstance().startApplication();
    }

    /**
     * Closing of application
     */
    @And("closeApp")
    public void closeApp() {
        AppManager.getInstance().stopApplication();
    }

    @And("openForm")
    public void openForm(String title) {
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
     *
     * @param pane  pane name
     * @param title tabbed pane title
     */
    @And("moveToTab")
    public void selectTabe(String pane, String title) {
        AppManager.getInstance().execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                ((JTabbedPaneOperator) TestContext.getCurrentForm().getComponentOperator(title)).selectPage(pane);
                return null;
            }
        });
    }


    /**
     * waining in seconds for a specified time
     *
     * @param wait
     */
    @And("wait")
    public void wait(String wait) {
        try {
            SECONDS.sleep(Long.parseLong(wait));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SwingBackRuntimeException(e);
        }
    }

    /**
     * Execute action with no parameters
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     */
    @And("userActionNoParams")
    public void userActionNoParams(String action) {
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
     * @param param  parameter
     */
    @And("userActionOneParam")
    public void userActionOneParam(String action, String param) {
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
     */
    @And("userActionTwoParams")
    public void userActionTwoParams(String action, String param1, String param2) {
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
     */
    @And("userActionThreeParams")
    public void userActionThreeParams(String action, String param1, String param2, String param3) {
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
     * @param action    title of the action to execute
     * @param dataTable table of parameters
     */
    @And("userActionTableParam")
    public void userActionTableParam(String action, DataTable dataTable) {
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
     * @param action    title of the action to execute
     * @param param     parameter
     * @param dataTable table of parameters
     */
    @And("userDoActionWithObject")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) {
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TestContext.getCurrentForm().executeMethodByTitle(action, param, dataTable);
                return null;
            }
        });
    }

}
