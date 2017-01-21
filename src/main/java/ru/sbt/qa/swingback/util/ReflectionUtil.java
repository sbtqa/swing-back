package ru.sbt.qa.swingback.util;

import ru.sbt.qa.swingback.Form;

/**
 * Created by sbt-varivoda-ia on 20.01.2017.
 */
public class ReflectionUtil {

    private static ReflectionUtil ourInstance = new ReflectionUtil();

    public static ReflectionUtil getInstance() {
        return ourInstance;
    }

    private ReflectionUtil() {
    }

    private static Class<? extends Form> getClassByFormEntryTitle(String packageName, String title) {
        return null;
    }
}
