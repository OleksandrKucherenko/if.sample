package com.sample.insurance.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for capturing logs during tests
 */
public class MemoryAppender extends ListAppender<ILoggingEvent> {
    public MemoryAppender(String loggerName) {
        setName("memory-appender");
        ((Logger) LoggerFactory.getLogger(loggerName)).addAppender(this);
        start();
    }
    
    public MemoryAppender(Class<?> loggerClass) {
        this(loggerClass.getName());
    }
    
    public List<ILoggingEvent> getLoggedEvents() {
        return Collections.unmodifiableList(list);
    }
    
    public List<String> getLoggedMessages() {
        return getLoggedEvents()
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .collect(Collectors.toList());
    }
    
    public List<String> getLoggedMessagesForLevel(Level level) {
        return getLoggedEvents()
                .stream()
                .filter(event -> event.getLevel().equals(level))
                .map(ILoggingEvent::getFormattedMessage)
                .collect(Collectors.toList());
    }
    
    public boolean logContains(String subString) {
        return getLoggedMessages()
                .stream()
                .anyMatch(message -> message.contains(subString));
    }
    
    public boolean logContainsExactly(String exactString) {
        return getLoggedMessages()
                .stream()
                .anyMatch(message -> message.equals(exactString));
    }
    
    public void reset() {
        list.clear();
    }
}
