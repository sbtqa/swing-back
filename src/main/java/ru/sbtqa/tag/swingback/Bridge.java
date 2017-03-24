package ru.sbtqa.tag.swingback;

import java.util.Locale;

/**
 * Class for transferring data to remote jvm
 */
public class Bridge {

    private static Locale locale;

    private Bridge() {
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        Bridge.locale = locale;
    }
}
