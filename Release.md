# 🚀 Spring Framework 7 & Spring Boot 4 — Overview & Migration Guide

This document provides a clear overview of the new features, improvements, and migration steps required for Spring Framework 7 and Spring Boot 4.

---

## Introduction

Spring Boot 4 is scheduled for release this November, with the latest preview currently available as Release Candidate 2 (RC2). This release marks a major evolution of the Spring ecosystem, arriving together with Spring Framework 7. Both versions introduce a broad set of improvements focused on modernization, performance, resilience, and long-term maintainability.

Spring Boot 4 and Spring Framework 7 move the platform forward in several key areas:

- **Modern Java Baseline** — Java 17+ is now required (Java 21 and 25 recommended), enabling modern JVM features such as sealed classes, pattern matching, and improved performance.
- **Jakarta Namespace Migration** — Full adoption of the `jakarta.*` namespace, completing the transition started in Spring Framework 6.
- **Updated Dependency Ecosystem** — Major upgrades across the stack, including Jackson 3.x, Hibernate 7, Tomcat/Netty updates, and Micrometer 2.x compatibility.
- **Improved AOT & Native Image Support** — Better build-time optimizations, enhanced GraalVM support, and fewer manual hints required.
- **Enhanced Observability** — Better integration with Micrometer, OpenTelemetry, and improved instrumentation defaults.
- **Built-in Resilience Features** — New annotations like `@Retryable`, `@ConcurrencyLimit`, and `@EnableResilientMethods` reduce the need for third-party libraries.
- **HTTP Client Modernization** — New declarative HTTP client capabilities via `@ImportHttpServices` and improved streaming APIs.
- **Testing Improvements** — New `RestTestClient` makes REST API testing lighter, faster, and more intuitive.
- **Simplified Path Matching** — Cleaner and faster routing behavior with improved `PathPattern` support.

This document provides a structured overview of the changes introduced in Spring Boot 4 and Spring Framework 7, along with the relevant migration steps, deprecations, and best practices to help development teams upgrade smoothly.


## 📌 Prerequisites

Spring Boot 4 and Spring Framework 7 require:

### ☕ **Java 17+**

Java 11 is no longer supported.

Upgrading to Java 17+ provides:

- Sealed classes
- Pattern matching for `switch`
- Removal of the Applet API
- Better JVM performance
- Improved support for GraalVM native images

## 🔧 Key Changes in Spring Framework 7

### 1. API Versioning Approaches

Two supported strategies:

- **Java configuration** using `WebMvcConfigurer`
- **Properties-based configuration** for centralized version mapping

---

### 2. Null Safety with JSpecify

Spring Framework 7 adopts **JSpecify annotations**, improving null-safety guarantees and Kotlin interoperability.

Key annotations:

- `@NullMarked` — everything non-null by default
- `@Nullable` — for optional parameters/fields
- `Optional<T>` — recommended for null-safe lookups (`findById`)

Supports safer:

- Partial updates
- Optional fields
- Nullable return types

---

### 3. Programmatic Bean Registration

`BeanRegistrar` allows:

- Dynamic bean creation
- Conditional bean registration
- Reduced boilerplate and higher flexibility

---

### 4. Optional Support in SpEL

Example:

```java
@Value("#{userService.findUser(#userId)?.orElse('Guest')}")
private String userName;
```

---

### 5. Resilience Features

Spring Framework 7 introduces built-in annotations for creating more resilient applications:

- **`@Retryable`** — Automatically retries failed method executions based on configured rules.
- **`@ConcurrencyLimit`** — Limits the number of concurrent executions for a method.
- **`@EnableResilientMethods`** — Enables resilience-related processing across your Spring beans.

These features eliminate the need for third-party resilience libraries like Resilience4j for common patterns.

---

### 6. Dedicated Configuration for HTTP Clients (`@ImportHttpServices`)

A new annotation, **`@ImportHttpServices`**, allows you to import and configure HTTP client interfaces in a declarative way.  
This simplifies the creation of typed HTTP clients and enhances maintainability of API integrations.

### 7. Streaming Support for HTTP Clients

Spring Framework 7 adds robust streaming support for HTTP clients:

- **`InputStream`** for large downloads
- **`OutputStream`** for large uploads

```java
@PostMapping("/upload")
public void uploadFile(InputStream inputStream) {
    // process large file stream
}
```

This is ideal for handling file transfer operations without loading the entire content into memory.

---

### 8. New `RestTestClient` for REST API Testing

A lightweight and modern alternative to `WebTestClient`, the **`RestTestClient`** provides:

- Simplified API testing
- Faster setup and execution
- Reduced overhead for unit and integration tests

This makes REST API testing more expressive and aligned with Spring’s testing philosophy.

---

### 9. Enhanced Path Matching with `PathPattern`

Spring 7 improves routing performance and correctness by using the updated **`PathPattern`** parser:

Benefits include:

- Faster URL matching
- More accurate pattern resolution
- Reduced ambiguity compared to the older AntPathMatcher

This is now the recommended approach for path parsing in Spring MVC applications.

---

### 10. New JmsClient and Enhancements to JdbcClient

The new JmsClient provides a modern API for working with JMS (Java Message Service), while JdbcClient has been enhanced for easier and more flexible database operations.

```java
   JmsClient jmsClient = JmsClient.create(connectionFactory);
   jmsClient.send("queue", "Hello World");

   JdbcClient jdbcClient = JdbcClient.create(dataSource);
   List<User> users = jdbcClient.sql("SELECT * FROM users").query(User.class).list();
```

## Depracations

### Migration Guide: Removed / Deprecated Components and Alternatives

| Removed / Deprecated                                             | Alternative                            |
| ---------------------------------------------------------------- | -------------------------------------- |
| `spring-jcl`                                                     | Apache Commons Logging                 |
| `javax.annotation`, `javax.inject`                               | `jakarta.annotation`, `jakarta.inject` |
| `suffixPatternMatch`, `trailingSlashMatch`, `favorPathExtension` | Explicit media types / URI templates   |
| XML-based Spring MVC configuration                               | Java-based `WebMvcConfigurer`          |
| JUnit 4                                                          | JUnit 5                                |
| Jackson 2.x                                                      | Jackson 3.x                            |

### Migration Checklist

- Upgrade to JDK 17+ (25 recommended)
- Replace javax._ with jakarta._
- Review MVC path matching - suffix & slash matching removed
- Move XML configs -> Java-based config
- Update tests: JUnit 5 + Jackson 3.x.
- Verify GraalVM native hints if using native images

## Important resources

- SpringFramework 7 [https://loiane.com/2025/08/spring-boot-4-spring-framework-7-key-features/]
