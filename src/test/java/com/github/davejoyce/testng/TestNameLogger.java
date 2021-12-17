package com.github.davejoyce.testng;

import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestNameLogger extends TestListenerAdapter {

    private static final ConcurrentMap<Class<?>, Boolean> RUNNING = new ConcurrentHashMap<>();

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        final Class<?> testClass = result.getTestClass().getRealClass();
        Boolean isRunning = RUNNING.putIfAbsent(testClass, Boolean.TRUE);
        if (!Boolean.TRUE.equals(isRunning)) {
            LoggerFactory.getLogger(testClass)
                    .debug("{} running", testClass.getSimpleName());
        }
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        clearRunning(tr.getTestClass().getRealClass());
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        clearRunning(tr.getTestClass().getRealClass());
    }

    protected void clearRunning(Class<?> testClass) {
        RUNNING.remove(testClass, Boolean.TRUE);
    }

}
