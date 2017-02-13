package ru.sbt.qa.swingback.aspects;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
/**
 * Aspect for translation an Exceptions to {@link AssertionError} and {@link NoSuchFieldException} errors <br/>
 */
public class ExceptionAspect {


    @AfterThrowing(pointcut = "execution(* *(..))", throwing = "throwable")
    public void translateException(Throwable throwable) throws Throwable {
        Throwable[] throwables = ExceptionUtils.getThrowables(throwable);
        Class<? extends Throwable> curThwClass;
        for (int i = 0; i < throwables.length; i++) {
            curThwClass = throwables[i].getClass();
            if (curThwClass.equals(AssertionError.class) || curThwClass.equals(NoSuchFieldException.class)) {
                throw throwables[i];
            }
        }
        throw throwable;
    }

}