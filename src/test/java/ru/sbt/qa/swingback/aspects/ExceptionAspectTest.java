package ru.sbt.qa.swingback.aspects;

import com.google.common.util.concurrent.ExecutionError;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class ExceptionAspectTest {

    ExceptionAspect ea;

    @Before
    public void setUp() throws Exception {
        ea = new ExceptionAspect();
    }

    @Test(expected = AssertionError.class)
    public void throwAssertionErrorWhenChainContainsAssertionError() throws Throwable {
        AssertionError error = new AssertionError("Ops");
        Error th = new Error(error);
        ExecutionError executionError = new ExecutionError(th);
        ea.translateException(executionError);
    }

    @Test(expected = NoSuchFieldException.class)
    public void throwNoSuchFieldExceptionWhenChainContainsNSFE() throws Throwable {
        NoSuchFieldException ex1 = new NoSuchFieldException();
        Error ex2 = new Error(ex1);
        ExecutionError executionError = new ExecutionError(ex2);
        ea.translateException(executionError);
    }

    @Test(expected = AssertionError.class)
    public void throwAssertionErrorWhenChainContainsAssertionErrorInMiddle() throws Throwable {
        Exception ex = new Exception();
        AssertionError error = new AssertionError(ex);
        ExecutionError executionError = new ExecutionError(error);
        ea.translateException(executionError);
    }

    @Test(expected = NoSuchMethodException.class)
    public void throwThrowableWhenChainNotContainsAssertionError() throws Throwable {
        NoSuchMethodException error = new NoSuchMethodException();
        ExecutionException executionException = new ExecutionException(error);
        ea.translateException(executionException);
    }

    @Test
    public void checkThatThrowablesCauseIsCorrectAfterAspect() throws Exception {
        NoSuchMethodException cause = new NoSuchMethodException();
        Exception ex2 = new Exception(cause);
        ExecutionException executionException = new ExecutionException(ex2);

        try {
            ea.translateException(executionException);
        } catch (Throwable throwable) {
            Assert.assertThat(cause, CoreMatchers.equalTo(throwable.getCause()));
        }
    }
}