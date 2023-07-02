package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TestExecutionTimeLogger implements TestRule {
    private final Logger log;
    private final StringBuilder testTimeSummary;

    public TestExecutionTimeLogger() {
        testTimeSummary = new StringBuilder();
        log = LoggerFactory.getLogger(TestExecutionTimeLogger.class);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.currentTimeMillis();
                try {
                    base.evaluate();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;
                    String methodName = description.getMethodName();
                    String executed = Objects.nonNull(methodName) ? methodName : description.getTestClass().getSimpleName();
                    testTimeSummary.append("\n").append(executed).append(": ").append(executionTime).append("ms");
                    log.info("{}: execution time = {}ms", executed, executionTime);
                }
            }
        };
    }

    public String getTimeSummary() {
        return testTimeSummary.toString();
    }
}
