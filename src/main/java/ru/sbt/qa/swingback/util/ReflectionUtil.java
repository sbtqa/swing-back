package ru.sbt.qa.swingback.util;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbt.qa.swingback.Form;
import ru.sbt.qa.swingback.annotations.FormEntry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sbt-varivoda-ia on 20.01.2017.
 */
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

        for (Class<?> form : allClasses) {
            String formTitle = form.getAnnotation(FormEntry.class).title();
            if (formTitle.equals(title)) {
                return form;
            }
        }
        return null;
    }
}
