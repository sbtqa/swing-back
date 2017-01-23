package ru.sbt.qa.swingback.util;

import com.google.common.reflect.ClassPath;
import ru.sbt.qa.swingback.Form;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by sbt-varivoda-ia on 20.01.2017.
 */
public class ReflectionUtil {

    private ReflectionUtil() {
    }

    private static Class<? extends Form> getClassByFormEntryTitle(String packageName, String title) {

//        ClassLoader loader = AbstractForm.class.getClassLoader();
//        logDebug("Thread from bank: " + Thread.currentThread().getName() + "\nThread loader " + Thread.currentThread()
//                .getContextClassLoader() + "\nAbstractForm loader " + AbstractForm.class
//                .getClassLoader());
//        try {
//            Optional<? extends Class<?>> resClassOpt;
//            resClassOpt = ClassPath.from(loader)
//                    .getAllClasses()
//                    .stream()
//                    .filter(classInfo -> classInfo.getName().startsWith(packageName + "."))
//                    .map(classInfo -> {
//                        try {
//                            logDebug("Название класса: " + classInfo.getName());
//                            return Class.forName(classInfo.getName());
//                        } catch (ClassNotFoundException e) {
//                            logError("Класс " + classInfo.getName() + " не найден!", e);
//                        }
//                        return null;
//                    })
//                    .filter(aClass -> {
//                        FormEntry annotation = aClass.getAnnotation(FormEntry.class);
//                        return annotation != null && annotation.title().equals(aTitle);
//                    })
//                    .findFirst();
//            if (!resClassOpt.isPresent()) {
//                throw new AutotestError("Форма с именем " + aTitle + " не найдена!");
//            }
//            return resClassOpt.get();
//        } catch (IOException ex) {
//            throw new AutotestError("Failed to shape class info set", ex);
//        }
        return null;
    }

    /**
     * Проверка на наличие аннотации {@link ComponentInfo} с определенным title у указанного поля.
     * @param aField Поле.
     * @param aTitle Название поля.
     * @return Наличие аннотации.
     * @author Varivoda Ivan
     */
    private static boolean fieldHasComponentInfoAnnWithTitle(Field aField, String aTitle) {
//        ComponentInfo componentInfo = aField.getAnnotation(ComponentInfo.class);
//        return componentInfo != null && componentInfo.title().equals(aTitle);
        return true;

    }


}
