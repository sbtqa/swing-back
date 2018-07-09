package ru.sbtqa.tag.swingback.stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import ru.sbtqa.tag.qautils.cucumber.CucumberUtils;
import ru.sbtqa.tag.swingback.AppManager;
import ru.sbtqa.tag.swingback.Bridge;

import java.util.Locale;
import java.util.concurrent.Callable;

public class SetUpStepDefinitions {

    Locale locale;

    public void setUp(Scenario scenario) {
        // Need to restart nano cloud
        AppManager.clearAppManager();
        //setting local through the bridge

        locale = CucumberUtils.getLocale(scenario);
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Bridge.setLocale(locale);
                return null;
            }
        });
    }
}
