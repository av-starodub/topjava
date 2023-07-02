package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestExecutionTimeLogger implements TestRule {
    private static final int NANO_IN_MILLIS = 1000000;
    private final Logger log;
    private final Map<String, Long> testExecutionTimeStatistics;
    private int methodNameMaxLength;

    public TestExecutionTimeLogger() {
        log = LoggerFactory.getLogger(TestExecutionTimeLogger.class);
        testExecutionTimeStatistics = new HashMap<>();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.nanoTime();
                try {
                    base.evaluate();
                } finally {
                    long endTime = System.nanoTime();
                    long executionTimeMillis = Math.round((double) (endTime - startTime) / NANO_IN_MILLIS);
                    String testMethodName = description.getMethodName();
                    if (Objects.nonNull(testMethodName)) {
                        methodNameMaxLength = Math.max(methodNameMaxLength, testMethodName.length());
                        testExecutionTimeStatistics.put(testMethodName, executionTimeMillis);
                        log.info("{}: execution time = {}ms", testMethodName, executionTimeMillis);
                    }
                }
            }
        };
    }

    public String createStatisticsRepresentation() {
        StringBuilder sb = new StringBuilder();
        testExecutionTimeStatistics.forEach((methodName, executionTimeMillis) -> sb
                .append("\n")
                .append(methodName)
                .append(repeat(" ", methodNameMaxLength - methodName.length() + 2))
                .append(executionTimeMillis)
                .append("ms")
        );
        return sb.toString();
    }

    private String repeat(String value, int count) {
        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < count; idx++) {
            sb.append(value);
        }
        return sb.toString();
    }
}
