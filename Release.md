## Spring Framework 7 and Springboot 4


## Prerequisites
1. Java 17+, Support for Java 11 has been dropped
    * For Sealed classes,
    * Pattern Matching for swith case
    * Removal of Applet API
    * Better performance with the JVM




## Key Changes


### Spring Framework 7 Key changes


1. Elegant API Versioning, in the @RequestMapping
2. Null Safety with JSpecify
3. Programmatic Bean Registration with BeanRegistrar
4. Improved Support for Optiona in SpEL (Spring Expression Language)
5. Resilience Features: @Retryable, @ConcurrencyLimit, @EnableResilientMethods
6. Dedicated Configuration for HTTP Clients with @ImportHttpServices
7. Streaming Support with InputStream and OutputStream in HTTP Clients
8. New RestTestClient for REST API Testing
9. Enhanced Path Matching with Improved PathPattern Support


## Depracations


### Migration Guide: Removed / Deprecated Components and Alternatives


| Removed / Deprecated                                      | Alternative                               |
|-----------------------------------------------------------|--------------------------------------------|
| `spring-jcl`                                              | Apache Commons Logging                     |
| `javax.annotation`, `javax.inject`                        | `jakarta.annotation`, `jakarta.inject`     |
| `suffixPatternMatch`, `trailingSlashMatch`, `favorPathExtension` | Explicit media types / URI templates |
| XML-based Spring MVC configuration                        | Java-based `WebMvcConfigurer`               |
| JUnit 4                                                   | JUnit 5                                     |
| Jackson 2.x                                               | Jackson 3.x                                 |


### Migration Checklist


* Upgrade to JDK 17+ (25 recommended)
* Replace javax.* with jakarta.*
* Review MVC path matching - suffix & slash matching removed
* Move XML configs -> Java-based config
* Update tests: JUnit 5 + Jackson 3.x.
* Verify GraalVM native hints if using native images


## Important resources


* SpringFramework 7 [https://loiane.com/2025/08/spring-boot-4-spring-framework-7-key-features/]



[//]: # (## Changelog)
## Spring Framework 7 and spring boot 4

1. Api Versioning: we have two approach ->
   - Java Configuration with WebMvcConfigurer
   - Properties based configuration:

2. NullSafety with Jspecify
   Spring Framework 7 has migrated to JSpecify annotations, improving null safety and integration with Kotlin. JSpecify provides more precise nullness contracts for method parameters, 	return types, and fields, helping developers catch potential null-related bugs at compile time

   - @NullMarked at class level (everything non-null by default)
   - @Nullable for optional fields (description)
   - @Nullable for methods that can return null (searchByName)
   - Optional for null-safe queries (findById)
   - Partial updates with nullable fields

3. Programmatic Bean Registration with BeanRegistrar

4. Improved Support for Optional in SpEL Expressions
   @Value("#{userService.findUser(#userId)?.orElse('Guest')}")
   private String userName;


5. Resilience Features: @Retryable, @ConcurrencyLimit, @EnableResilientMethods

New annotations like @Retryable and @ConcurrencyLimit help build more resilient applications by enabling retry logic and concurrency limits directly in your service methods. The @EnableResilientMethods annotation activates these features for your beans.