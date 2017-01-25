package ru.sbt.qa.swingback.util;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbt.qa.swingback.Form;
import ru.sbt.qa.swingback.annotations.FormEntry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil {

    public static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    private ReflectionUtil() {
    }

    public static Class<?> getClassByFormEntryTitle(String packageName, String title) {
        //TODO: 23.01.2017
        ClassLoader loader = Form.class.getClassLoader();
        Set<Class<?>> allClasses = new HashSet<>();
        try {
            ClassPath.from(loader)
                    .getAllClasses()
                    .stream()
                    .filter((info) -> (info.getName().startsWith(packageName + ".")))
                    .forEach((info) -> {
                        allClasses.add(info.load());
                    });
        } catch (IOException ex) {
            log.warn("Failed to shape class info set", ex);
        }

        FormEntry formEntry;
        for (Class<?> form : allClasses) {
            System.out.println("Now look up " + form);
            formEntry = form.getAnnotation(FormEntry.class);
            if (formEntry != null && formEntry.title().equals(title)) {
                return form;
            }
        }
        return null;
    }
}
