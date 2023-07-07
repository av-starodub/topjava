package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestExecutionTimeLogger extends Stopwatch {
    private final Logger log;
    private final Map<String, Long> testExecutionTimeStatistics;
    private final Deque<String> testExecutionOrder;
    private int methodNameMaxLength;

    public TestExecutionTimeLogger() {
        log = LoggerFactory.getLogger(TestExecutionTimeLogger.class);
        testExecutionTimeStatistics = new HashMap<>();
        testExecutionOrder = new ArrayDeque<>();
    }

    @Override
    protected void finished(long nanos, Description description) {
        if (description.isTest()) {
            String testMethodName = description.getMethodName();
            Long executionTimeMillis = TimeUnit.NANOSECONDS.toMillis(nanos);
            methodNameMaxLength = Math.max(methodNameMaxLength, testMethodName.length());
            testExecutionOrder.addLast(testMethodName);
            testExecutionTimeStatistics.put(testMethodName, executionTimeMillis);
            log.info("{}: execution time = {}ms", testMethodName, executionTimeMillis);
        }
    }

    public String createStatisticsRepresentation() {
        StringBuilder sb = new StringBuilder();
        String alignRightTemplate = String.format("%s%d%s %s", "%-", (methodNameMaxLength + 2), "s", "%d");
        testExecutionOrder.forEach((methodName) -> sb
                .append("\n")
                .append(String.format(alignRightTemplate, methodName, testExecutionTimeStatistics.get(methodName))));
        return sb.toString();
    }
}
