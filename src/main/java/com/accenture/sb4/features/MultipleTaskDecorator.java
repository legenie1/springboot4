package com.accenture.sb4.features;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskDecorator;

/**
 * Spring Boot 4 Feature: Multiple TaskDecorator Support
 *
 * TaskDecorators allow you to wrap async tasks with cross-cutting concerns like:
 * - Distributed tracing (MDC propagation)
 * - Security context propagation
 * - Logging
 * - Metrics
 *
 * Spring Boot 4 now supports MULTIPLE TaskDecorators with ordering!
 */
@Configuration
public class MultipleTaskDecorator {

    @Bean
    @Order(1)
    public TaskDecorator tracingDecorator() {
        return runnable -> () -> {
            System.out.println("Tracing Start");
            try {
                runnable.run();
            } finally {
                System.out.println("Tracing End");
            }
        };
    }

    @Bean
    @Order(2)
    public TaskDecorator loggingDecorator() {
        return runnable -> () -> {
            System.out.println("Logging Start");
            try {
                runnable.run();
            } finally {
                System.out.println("Logging End");
            }
        };
    }
}