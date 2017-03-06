package ru.sbtqa.tag.swingback.aspects;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.netbeans.jemmy.TimeoutExpiredException;

@Aspect
/**
 * Aspect for translation an Exceptions to {@link AssertionError}, {@link NoSuchFieldException}, {@link NoSuchMethodException}, {@link TimeoutExpiredException} errors <br/>
 */
public class ExceptionAspect {


    @AfterThrowing(pointcut = "execution(* ru.sbtqa.tag.swingback.AppManager.execute(..))", throwing = "throwable")
    public void translateException(Throwable throwable) throws Throwable {
        Throwable[] throwables = ExceptionUtils.getThrowables(throwable);
        Class<? extends Throwable> curThwClass;
        for (int i = 0; i < throwables.length; i++) {
            curThwClass = throwables[i].getClass();
            if (curThwClass.equals(AssertionError.class) || curThwClass.equals(NoSuchFieldException.class) || curThwClass.equals(NoSuchMethodException.class) || curThwClass.equals(TimeoutExpiredException.class)) {
                throw throwables[i];
            }
        }
        throw throwable.getCause();
    }

}