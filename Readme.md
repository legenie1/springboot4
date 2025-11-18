# Spring Framework 7 & Spring Boot 4 - Complete Guide

## Table of Contents
- [Overview](#overview)
- [Spring Framework 7](#spring-framework-7)
- [Spring Boot 4](#spring-boot-4)
- [Prerequisites](#prerequisites)
- [Migration Guide](#migration-guide)
- [Best Practices](#best-practices)
- [Resources](#resources)

---

## Overview
This document provides comprehensive information about **Spring Framework 7** and **Spring Boot 4**, representing the next generation of enterprise Java application development. These versions introduce modern Java features, enhanced performance, improved developer experience, and first-class support for cloud-native architectures.

**Release Status**: Spring Boot 4.0.0-RC2 (Release Candidate) - November 2025  
**General Availability**: Expected Q1 2026

---

## Spring Framework 7

### Introduction
Spring Framework 7 is the foundation for Spring Boot 4, bringing significant architectural improvements and modern Java language support. It represents a major evolution in the Spring ecosystem with focus on performance, observability, and cloud-native development.

### Key Features

#### 1. **Java 21 LTS Baseline**
- **Minimum Requirement**: Java 21 is now the baseline (no longer supports Java 17)
- **Virtual Threads (Project Loom)**:
  - Native support for virtual threads for improved scalability
  - Simplified concurrent programming model
  - Reduced resource consumption for I/O-bound applications
  - Automatic virtual thread usage in web containers
- **Pattern Matching & Records**:
  - Enhanced switch expressions with pattern matching
  - Better support for Java records as DTOs and entities
  - Simplified data modeling with immutable objects

#### 2. **Core Container Enhancements**
- **Improved Bean Registration**:
  - Faster application context startup
  - Optimized bean definition processing
  - Enhanced circular dependency detection
- **AOT (Ahead-of-Time) Processing**:
  - Build-time reflection configuration generation
  - Improved native image compatibility
  - Reduced runtime overhead
- **Constructor Injection Improvements**:
  - Better support for Lombok and records
  - Simplified dependency injection patterns

#### 3. **Reactive Programming Evolution**
- **Project Reactor 2023.x**:
  - Updated to latest Reactor release
  - Better context propagation
  - Enhanced operator performance
- **WebFlux Improvements**:
  - Virtual thread integration for reactive applications
  - Improved backpressure handling
  - Enhanced HTTP/2 and HTTP/3 support

#### 4. **Data Access Modernization**
- **JPA & Hibernate 6.4+**:
  - Better support for modern database features
  - Improved query performance
  - Enhanced batch processing
- **R2DBC Updates**:
  - Reactive database connectivity improvements
  - Better transaction management
  - Expanded database support
- **JDBC Enhancements**:
  - Virtual thread support for blocking operations
  - Simplified exception handling
  - Better connection pool integration

#### 5. **Web Framework Updates**
- **Spring MVC**:
  - Virtual thread execution model
  - Enhanced request processing pipeline
  - Improved multipart file handling
  - Better support for modern HTTP methods
- **WebSocket Improvements**:
  - Enhanced message handling
  - Better connection management
  - Improved scalability with virtual threads

#### 6. **Security Framework Integration**
- **Spring Security 7**:
  - OAuth 2.1 support
  - Enhanced JWT handling
  - Improved authorization mechanisms
  - Better integration with identity providers
- **Method Security**:
  - Simplified annotation-based security
  - Enhanced expression language
  - Better aspect-oriented security

#### 7. **Testing Framework**
- **JUnit 5.10+**:
  - Enhanced test execution
  - Better parameterized tests
  - Improved test lifecycle management
- **MockMvc Improvements**:
  - Better async testing support
  - Enhanced request/response handling
  - Improved assertion capabilities

---

## Spring Boot 4

### Introduction
Spring Boot 4 builds on Spring Framework 7 to provide an opinionated, production-ready platform for building stand-alone, cloud-native applications with minimal configuration.

### Major Features

#### 1. **Java 21 LTS Support**
- **Required**: Java 21 is mandatory (Java 17 no longer supported)
- **Virtual Threads Everywhere**:
  - Enabled by default in Tomcat, Jetty, and Undertow
  - Automatic configuration for thread pools
  - Configuration property: `spring.threads.virtual.enabled=true`
- **Modern Language Features**:
  - Pattern matching in business logic
  - Record classes for configuration properties
  - Text blocks for SQL and JSON templates

#### 2. **Native Compilation & GraalVM**
- **Simplified Native Image Build**:
  - Enhanced Spring AOT engine
  - Automatic hint generation for reflection, resources, and proxies
  - Build plugin improvements: `mvn spring-boot:build-image -Pnative`
- **Performance Benefits**:
  - Startup time: < 100ms for typical applications
  - Memory footprint: 50-70% reduction
  - Zero warmup time
- **Docker & Kubernetes Ready**:
  - Optimized container images
  - Smaller image sizes (20-50 MB)
  - Faster pod startup in Kubernetes

#### 3. **Enhanced Observability**
- **Micrometer 1.13+**:
  - Unified metrics collection
  - Support for multiple monitoring backends (Prometheus, DataDog, New Relic)
  - Custom metrics simplified with annotations
- **Distributed Tracing**:
  - OpenTelemetry as first-class citizen
  - Automatic trace propagation across services
  - Simplified correlation IDs and baggage
- **Structured Logging**:
  - JSON logging by default option
  - Better log correlation with trace IDs
  - Enhanced log aggregation support

#### 4. **Declarative HTTP Clients**
- **HTTP Interface**:
  ```java
  @HttpExchange("/api/users")
  public interface UserClient {
      @GetExchange("/{id}")
      User getUser(@PathVariable Long id);
      
      @PostExchange
      User createUser(@RequestBody User user);
  }
  ```
- **Auto-Configuration**:
  - Automatic client bean creation
  - Integrated with RestClient and WebClient
  - Built-in retry, timeout, and circuit breaker support
- **Replaces**:
  - Feign clients (legacy)
  - Manual RestTemplate configuration

#### 5. **RestClient API**
- **Modern Synchronous HTTP Client**:
  ```java
  RestClient client = RestClient.builder()
      .baseUrl("https://api.example.com")
      .defaultHeader("Authorization", "Bearer token")
      .build();
      
  User user = client.get()
      .uri("/users/{id}", 123)
      .retrieve()
      .body(User.class);
  ```
- **Features**:
  - Fluent API design
  - Virtual thread compatible
  - Better than legacy RestTemplate

#### 6. **Docker Compose Integration**
- **Automatic Service Startup**:
  - Detects `compose.yaml` in project root
  - Starts dependencies during application startup
  - Automatic shutdown on application stop
- **Configuration**:
  ```yaml
  spring:
    docker:
      compose:
        enabled: true
        file: compose.yaml
        lifecycle-management: start-and-stop
  ```
- **Supported Services**:
  - Databases (PostgreSQL, MySQL, MongoDB, Redis)
  - Message brokers (Kafka, RabbitMQ)
  - Infrastructure (Elasticsearch, Zipkin, Prometheus)

#### 7. **SSL Bundle Management**
- **Centralized SSL Configuration**:
  ```yaml
  spring:
    ssl:
      bundle:
        jks:
          my-bundle:
            keystore:
              location: classpath:keystore.p12
              password: secret
            truststore:
              location: classpath:truststore.p12
  ```
- **Reusable Across Components**:
  - Web servers
  - HTTP clients
  - Database connections
- **Auto-Reload Support**:
  - Watch for certificate changes
  - No downtime certificate rotation

#### 8. **Enhanced Auto-Configuration**
- **Conditional Configuration Improvements**:
  - Better ordering and dependency resolution
  - Reduced startup time
  - Clearer configuration reports
- **Configuration Properties**:
  - Better validation with Bean Validation 3.0
  - Support for Java records
  - Immutable configuration classes
- **Failure Analysis**:
  - More detailed error messages
  - Better hints for misconfiguration
  - Improved diagnostics

#### 9. **Testcontainers Support**
- **First-Class Integration**:
  ```java
  @SpringBootTest
  @ServiceConnection
  class ApplicationTests {
      @Container
      static PostgreSQLContainer<?> postgres = 
          new PostgreSQLContainer<>("postgres:16");
  }
  ```
- **Dynamic Properties**:
  - Automatic connection string configuration
  - No manual property overrides needed
- **Dev Services**:
  - Automatic test database provisioning
  - Container reuse across test runs

#### 10. **Virtual Thread Support**
- **Platform-Wide Integration**:
  - Web request handling
  - Async task execution
  - Scheduled tasks
  - @Async methods
- **Configuration**:
  ```yaml
  spring:
    threads:
      virtual:
        enabled: true
  ```
- **Benefits**:
  - Handle 100,000+ concurrent requests
  - Simplified concurrent programming
  - Better resource utilization

#### 11. **Problem Details (RFC 7807)**
- **Standardized Error Response**:
  ```java
  @ExceptionHandler
  ProblemDetail handleNotFound(ResourceNotFoundException ex) {
      ProblemDetail pd = ProblemDetail.forStatusAndDetail(
          HttpStatus.NOT_FOUND, 
          ex.getMessage()
      );
      pd.setProperty("timestamp", Instant.now());
      return pd;
  }
  ```
- **Features**:
  - Standard error format across services
  - Better API client experience
  - Automatic content negotiation

#### 12. **Dependency Updates**
- **Jakarta EE 11**: Latest enterprise Java standards
- **Hibernate 6.4+**: Modern ORM features
- **Jackson 2.17+**: Enhanced JSON processing
- **Netty 4.1.100+**: Better reactive networking
- **Micrometer 1.13+**: Advanced metrics
- **Reactor 2023.x**: Latest reactive streams

---

## Prerequisites

### Required Software
1. **Java Development Kit**:
   - **Java 21 LTS** (minimum required)
   - Recommended: Oracle JDK 21, Eclipse Temurin 21, or Amazon Corretto 21
   - Virtual threads feature available out-of-the-box

2. **Build Tools**:
   - **Maven**: 3.9.0+ (recommended 3.9.5+)
   - **Gradle**: 8.5+ (recommended 8.7+)

3. **IDE Support**:
   - **IntelliJ IDEA**: 2024.1+ (with Spring Boot plugin)
   - **Eclipse**: 2024-03+ (with Spring Tools 4)
   - **VS Code**: Latest with Spring Boot Extension Pack

4. **Optional - Native Image**:
   - **GraalVM**: 23.0+ for Java 21 (Community or Enterprise)
   - Native Image component installed: `gu install native-image`
   - C/C++ toolchain (GCC for Linux, MSVC for Windows, XCode for macOS)

### System Requirements
- **RAM**: Minimum 4GB, Recommended 8GB+ for development
- **Disk Space**: 2GB for dependencies and build artifacts
- **OS**: Windows 10/11, macOS 11+, Linux (modern distributions)

---

## Migration Guide

### Step 1: Update Java Version
```bash
# Verify Java 21 installation
java -version  # Should show version 21.x.x

# Update JAVA_HOME environment variable
# Windows PowerShell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Linux/macOS
export JAVA_HOME=/path/to/jdk-21
```

### Step 2: Update Build Configuration

#### Maven (pom.xml)
```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.0-RC2</version>
    <relativePath/>
</parent>

<dependencies>
    <!-- Core Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Observability -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    
    <!-- Add other starters as needed -->
</dependencies>
```

#### Gradle (build.gradle)
```groovy
plugins {
    id 'org.springframework.boot' version '4.0.0-RC2'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'java'
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### Step 3: Enable Virtual Threads
```yaml
# application.yaml
spring:
  threads:
    virtual:
      enabled: true
```

### Step 4: Update Dependencies
Common dependency changes from Spring Boot 3.x:

| Old (Boot 3.x) | New (Boot 4.0) | Notes |
|----------------|----------------|-------|
| `javax.*` | `jakarta.*` | Already done in Boot 3.x |
| RestTemplate | RestClient | RestTemplate deprecated |
| Feign | @HttpExchange | Declarative clients preferred |
| Sleuth | Micrometer Tracing | Built-in observability |

### Step 5: Configuration Property Updates
```yaml
# Update deprecated properties
spring:
  # OLD: spring.mvc.async.request-timeout
  # NEW:
  mvc:
    async:
      request-timeout: 30s
  
  # Enable new features
  docker:
    compose:
      enabled: true  # Auto-start Docker Compose services
  
  # Observability
  management:
    tracing:
      sampling:
        probability: 1.0  # 100% sampling for dev
    metrics:
      export:
        prometheus:
          enabled: true
```

### Step 6: Code Updates

#### Replace RestTemplate with RestClient
```java
// OLD (Spring Boot 3.x)
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}

// Usage
User user = restTemplate.getForObject("https://api.example.com/users/{id}", User.class, 123);

// NEW (Spring Boot 4.0)
@Bean
public RestClient restClient() {
    return RestClient.builder()
        .baseUrl("https://api.example.com")
        .build();
}

// Usage
User user = restClient.get()
    .uri("/users/{id}", 123)
    .retrieve()
    .body(User.class);
```

#### Use Declarative HTTP Clients
```java
// Define interface
@HttpExchange("/api")
public interface ApiClient {
    @GetExchange("/users/{id}")
    User getUser(@PathVariable Long id);
    
    @PostExchange("/users")
    User createUser(@RequestBody User user);
}

// Configuration
@Configuration
public class ClientConfig {
    @Bean
    public ApiClient apiClient(RestClient.Builder builder) {
        RestClient client = builder
            .baseUrl("https://api.example.com")
            .build();
        HttpServiceProxyFactory factory = 
            HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client))
                .build();
        return factory.createClient(ApiClient.class);
    }
}
```

#### Use Records for Configuration
```java
// Configuration properties with records
@ConfigurationProperties("app")
public record AppProperties(
    String name,
    String version,
    Database database
) {
    public record Database(String url, String username) {}
}
```

### Step 7: Testing Updates
```java
// Use @ServiceConnection for Testcontainers
@SpringBootTest
class ApplicationTests {
    
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:16-alpine");
    
    @Test
    void testDatabase() {
        // Database automatically configured
    }
}
```

### Step 8: Verify Build
```bash
# Maven
mvn clean verify

# Gradle
gradle clean build

# Run application
mvn spring-boot:run
# or
gradle bootRun
```

---

## Best Practices

### 1. Virtual Threads
```java
// Enable for all async operations
@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {
        TaskExecutorAdapter adapter = new TaskExecutorAdapter(
            Executors.newVirtualThreadPerTaskExecutor()
        );
        return adapter;
    }
}

// Use with @Async
@Async
public CompletableFuture<String> processAsync() {
    // Virtual thread handles this
    return CompletableFuture.completedFuture("Done");
}
```

### 2. Observability
```java
// Add custom metrics
@Component
public class BusinessMetrics {
    private final Counter orderCounter;
    
    public BusinessMetrics(MeterRegistry registry) {
        this.orderCounter = Counter.builder("orders.created")
            .tag("type", "online")
            .register(registry);
    }
    
    public void recordOrder() {
        orderCounter.increment();
    }
}

// Add custom spans
@Service
public class OrderService {
    private final ObservationRegistry registry;
    
    public void processOrder(Order order) {
        Observation.createNotStarted("order.processing", registry)
            .lowCardinalityKeyValue("order.type", order.getType())
            .observe(() -> {
                // Business logic here
            });
    }
}
```

### 3. Native Image Hints
```java
// For reflection
@RegisterReflectionForBinding(User.class)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// For runtime hints
@Configuration
@ImportRuntimeHints(MyRuntimeHints.class)
public class AppConfig {}

class MyRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
            .registerType(MyClass.class, MemberCategory.INVOKE_PUBLIC_METHODS);
    }
}
```

### 4. Error Handling
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
        pd.setType(URI.create("https://api.example.com/errors/not-found"));
        pd.setTitle("Resource Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
```

### 5. Configuration Properties
```yaml
# application.yaml - Use type-safe configuration
app:
  name: MyApplication
  version: 1.0.0
  features:
    virtual-threads: true
    observability: true
  database:
    url: jdbc:postgresql://localhost:5432/mydb
    pool-size: 20
```

```java
@ConfigurationProperties("app")
public record AppConfig(
    String name,
    String version,
    Features features,
    Database database
) {
    public record Features(boolean virtualThreads, boolean observability) {}
    public record Database(String url, int poolSize) {}
}
```

---

## Resources

### Official Documentation
- [Spring Framework 7 Documentation](https://docs.spring.io/spring-framework/docs/7.0.x/reference/html/)
- [Spring Boot 4 Reference Guide](https://docs.spring.io/spring-boot/docs/4.0.x/reference/html/)
- [Spring Boot 4 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)

### Learning Resources
- [Spring Academy](https://spring.academy/) - Official training courses
- [Spring Blog](https://spring.io/blog) - Latest updates and tutorials
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

### Community
- [Spring on Stack Overflow](https://stackoverflow.com/questions/tagged/spring-boot)
- [Spring Community Forum](https://community.spring.io/)
- [GitHub Issues](https://github.com/spring-projects/spring-boot/issues)

### Tools & Plugins
- [Spring Initializr](https://start.spring.io/) - Project generator (supports Boot 4)
- [Spring Boot CLI](https://docs.spring.io/spring-boot/docs/current/reference/html/cli.html)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)

### Performance & Monitoring
- [Micrometer Documentation](https://micrometer.io/docs)
- [OpenTelemetry Java](https://opentelemetry.io/docs/instrumentation/java/)
- [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)

---

## Quick Start Example

```java
// Modern Spring Boot 4 Application
@SpringBootApplication
public class ModernApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModernApplication.class, args);
    }
}

// RESTful Controller with virtual threads
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService service;
    private final UserClient externalClient;
    
    public UserController(UserService service, UserClient externalClient) {
        this.service = service;
        this.externalClient = externalClient;
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return service.save(user);
    }
}

// Declarative HTTP Client
@HttpExchange("https://api.external.com/users")
public interface UserClient {
    @GetExchange("/{id}")
    ExternalUser fetchUser(@PathVariable Long id);
}

// Configuration with records
@ConfigurationProperties("app")
public record AppProperties(String name, String version) {}
```

```yaml
# application.yaml
spring:
  application:
    name: modern-app
  threads:
    virtual:
      enabled: true
  docker:
    compose:
      enabled: true
  management:
    endpoints:
      web:
        exposure:
          include: health,info,metrics,prometheus
    tracing:
      sampling:
        probability: 1.0

app:
  name: Modern Spring Boot App
  version: 1.0.0
```

---

**Document Version**: 2.0
**Last Updated**: November 2025  
**Compatibility**: Spring Framework 7.0.x, Spring Boot 4.0.0-RC2+