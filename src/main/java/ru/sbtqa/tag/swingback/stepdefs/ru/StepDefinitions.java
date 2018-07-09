package ru.sbtqa.tag.swingback.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.swingback.stepdefs.GenericStepDefinitions;

public class StepDefinitions extends GenericStepDefinitions {

    @Before
    public void setUp(Scenario scenario) {
        super.setUp(scenario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)открывает приложение$")
    public void startApp() {
        super.startApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)закрывает приложение$")
    public void closeApp() {
        super.closeApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^открывается форма \"(.*?)\"$")
    public void openForm(String title) {
        super.openForm(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)переходит на вкладку \"(.*?)\" в окне с вкалдками \"(.*?)\"$")
    public void selectTabe(String pane, String title) {
        super.selectTabe(pane, title);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @And("^ожидание (.*?) сек$")
    public void wait(String wait) {
        super.wait(wait);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он)\\((.*?)\\)$")
    public void userActionNoParams(String action) {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он)\\((.*?)\\) (?:с параметром |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрарми |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрарми |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)\\((.*?)\\) данными$")
    public void userActionTableParam(String action, DataTable dataTable) {
        userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он |)\\((.*?)\\) [^\"]*\"([^\"]*)\" данными$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) {
        super.userDoActionWithObject(action, param, dataTable);
    }

}

