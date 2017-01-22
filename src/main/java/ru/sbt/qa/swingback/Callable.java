package ru.sbt.qa.swingback;

import java.io.Serializable;

/**
 * Created by Varivoda Ivan on 22.01.2017.
 */
public interface Callable<T> extends java.util.concurrent.Callable<T>, Serializable {
}
