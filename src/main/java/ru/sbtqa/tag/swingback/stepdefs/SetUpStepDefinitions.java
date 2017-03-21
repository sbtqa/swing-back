package ru.sbtqa.tag.swingback.stepdefs;

import cucumber.api.java.Before;
import ru.sbtqa.tag.cucumber.TagCucumber;
import ru.sbtqa.tag.swingback.AppManager;
import ru.sbtqa.tag.swingback.Bridge;

import java.util.Locale;
import java.util.concurrent.Callable;

public class SetUpStepDefinitions {

    @Before
    public void setUp() {
        Locale locale = TagCucumber.getFeature().getI18n().getLocale();
        AppManager.getInstance().execute(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Bridge.setLocale(locale);
                return null;
            }
        });
    }
}
