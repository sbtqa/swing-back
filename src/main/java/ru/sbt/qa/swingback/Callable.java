package ru.sbt.qa.swingback;

import java.io.Serializable;

public interface Callable<T> extends java.util.concurrent.Callable<T>, Serializable {
}
