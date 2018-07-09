package ru.sbtqa.tag.swingback.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.swingback.stepdefs.GenericStepDefinitions;

public class StepDefinitions extends GenericStepDefinitions {

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user open the application$")
    public void startApp() {
        super.startApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user close the application$")
    public void closeApp() {
        super.closeApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^opening form \"(.*?)\"$")
    public void openForm(String title) {
        super.openForm(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user select the tab \"(.*?)\" on the tabbed pane \"(.*?)\"$")
    public void selectTabe(String pane, String title) {
        super.selectTabe(pane, title);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @And("^wait (.*?) sec$")
    public void wait(String wait) {
        super.wait(wait);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |)\\((.*?)\\)$")
    public void userActionNoParams(String action) {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |)\\((.*?)\\) (?:with param |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) data$")
    public void userActionTableParam(String action, DataTable dataTable) {
        userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) [^\"]*\"([^\"]*) data$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) {
        super.userDoActionWithObject(action, param, dataTable);
    }

}

