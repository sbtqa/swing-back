package ru.sbt.qa.swingback.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.util.concurrent.ExecutionException;

@Aspect
/**
 * Aspect for translation an Exceptions to AssertationError errors <br/>
 * to split infrastructure errors from functional errors
 *
 *
 * @version $Id: $Id
 */
public class ExceptionAspect {

    static long lastFailureTimestamp = 0;

    /**
     * <p>
     * translatexception.</p>
     *
//     * @param joinPoint a {@link ProceedingJoinPoint} object.
     * @return a {@link Object} object.
     * @throws Throwable if any.
     */
//     && within(ru.sbt.qa.swingback.Form) && !within(ru.sbtqa.tag.bdd.util.Allure*)
    @AfterThrowing(pointcut = "execution(* ru.sbt.qa.swingback.Form.executeMethodByTitle(..))", throwing = "error")
    public void translateException(Throwable error) throws Throwable {
        System.err.println("Yeee");
        if (error.getCause().getCause() instanceof AssertionError) {
            throw error.getCause().getCause();
        } else {
            throw new AutotestError("Вранье", error);
        }
    }

    @AfterThrowing(pointcut = "execution(* org.gridkit.util.concurrent.FutureBox.get(..))", throwing = "error")
    public void translateException(ExecutionException error) throws Throwable {
        System.err.println("Yeee");
        if (error.getCause() instanceof AssertionError) {
            throw error.getCause();
        } else {
            throw new AutotestError("Вранье", error);
        }
    }


    }

