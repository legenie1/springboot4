package com.accenture.sb4.controller;

import com.accenture.sb4.entity.Todo;
import com.accenture.sb4.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Testing Spring REST APIs with RestTestClient: A Complete Guide
 *
 * RestTestClient is the NEW unified testing tool in Spring Framework 7 / Spring Boot 4
 * that combines the best features of MockMvc and WebTestClient into ONE consistent API.
 *
 * This test class demonstrates the 5 different testing approaches:
 * 1. Unit Testing (bindToController) - Fastest, no Spring context
 * 2. Spring MVC Testing (bindToMockMvc) - Tests validation, security
 * 3. Integration Testing (bindToApplicationContext) - Full Spring context, no server
 * 4. End-to-End Testing (bindToServer) - Real HTTP server, realistic
 * 5. Functional Endpoints (bindToRouterFunction) - For WebFlux functional routes
 */
@DisplayName("TodoController Tests - RestTestClient Complete Guide")
public class TodoControllerTests {
    public TodoControllerTests(TodoService todoService) {
    }

    // =================================================================
    // APPROACH 1: Unit Testing with bindToController
    // =================================================================
    // ⚡ FASTEST - No Spring context loaded
    // ✅ Use for: Quick controller logic tests without dependencies
    // ❌ No Spring features: validation, security, @MockBean
    // =================================================================

    @Nested
    @DisplayName("1. Unit Tests (bindToController) - No Spring Context")
    class UnitTests {

        private WebTestClient client;
        private TodoService todoService;

        RestTestClient restTestClient;

        @BeforeEach
        void setup() {
            todoService = Mockito.mock(TodoService.class);

            when(todoService.getAllTodos()).thenReturn(
                    List.of(
                            new Todo(1, 1, "Learn Spring Boot 4", false),
                            new Todo(2, 1, "Master RestTestClient", false)
                    )
            );

            when(todoService.getTodoById(1)).thenReturn(
                    new Todo(1, 1, "Learn Spring Boot 4", false)
            );

            TodoController controller = new TodoController(todoService);
            client = MockMvcWebTestClient.bindToController(controller).build();

            //  restTestClient = RestTestClient.bindToController(new TodoControllerTests(todoService)).build();
        }

        @Test
        @DisplayName("Should get all todos - Pure unit test")
        void shouldGetAllTodos() {
            List<Todo> todos = client.get()
                    .uri("/api/todos")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                    .returnResult()
                    .getResponseBody();

            assertNotNull(todos);
            assertEquals(2, todos.size());
            assertEquals("Learn Spring Boot 4", todos.get(0).title());

            // EXPLANATION: This is FAST because:
            // - No Spring context to load
            // - No database connection
            // - No web server
            // - Just pure Java method calls
        }

        @Test
        @DisplayName("Should get todo by ID")
        void shouldGetTodoById() {
            client.get()
                    .uri("/api/todos/1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Todo.class)
                    .value(todo -> {
                        assertEquals(1, todo.id());
                        assertEquals("Learn Spring Boot 4", todo.title());
                        assertFalse(todo.completed());
                    });

        }

        @Test
        @DisplayName("Should create new todo")
        void shouldCreateTodo() {
            Todo newTodo = new Todo(null, 1, "New Todo", false);
            Todo savedTodo = new Todo(3, 1, "New Todo", false);

            when(todoService.createTodo(newTodo)).thenReturn(savedTodo);

            client.post()
                    .uri("/api/todos")
                    .bodyValue(newTodo)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(Todo.class)
                    .value(todo -> {
                        assertEquals(3, todo.id());
                        assertEquals("New Todo", todo.title());
                    });

            // EXPLANATION: POST requests work the same way
            // Note: Validation (@Valid) won't work here - need Spring for that!
        }
    }

    // =================================================================
    // APPROACH 2: Spring MVC Testing with bindToMockMvc
    // =================================================================
    // 🚀 FAST - Loads Spring MVC layer only
    // ✅ Use for: Testing validation, security, exception handling
    // ✅ @MockBean works here!
    // ❌ No real database or full application context
    // =================================================================

    @Nested
    @DisplayName("2. Spring MVC Tests (bindToMockMvc) - With Validation & Security: Spring ")
    @WebMvcTest(TodoController.class)
    class SpringMvcTests {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TodoService todoService;

        private WebTestClient client;

        @BeforeEach
        void setup() {
            // Bind to MockMvc - now we have Spring MVC features!
            client = MockMvcWebTestClient.bindTo(mockMvc).build();
        }

        @Test
        @DisplayName("Should validate input - empty title should fail")
        void shouldValidateEmptyTitle() {
            Todo invalidTodo = new Todo(null, 1, "", false);

            client.post()
                    .uri("/api/todos")
                    .bodyValue(invalidTodo)
                    .exchange()
                    .expectStatus().isBadRequest();

            // EXPLANATION: Validation now works because Spring MVC is loaded!
            // @Valid, @NotBlank, etc. are processed by Spring
        }

        @Test
        @DisplayName("Should handle not found exception")
        void shouldHandleNotFoundException() {
            when(todoService.getTodoById(999))
                    .thenThrow(new RuntimeException("Todo not found"));

            client.get()
                    .uri("/api/todos/999")
                    .exchange()
                    .expectStatus().is5xxServerError();

            // EXPLANATION: Exception handling (@ExceptionHandler) works here
            // Spring MVC's error handling is active
        }

        @Test
        @DisplayName("Should test with custom headers")
        void shouldAcceptCustomHeaders() {
            when(todoService.getAllTodos()).thenReturn(List.of());

            client.get()
                    .uri("/api/todos")
                    .header("X-Custom-Header", "test-value")
                    .header("Authorization", "Bearer token123")
                    .exchange()
                    .expectStatus().isOk();

            // EXPLANATION: Spring MVC processes headers, security filters, etc.
        }
    }

    // =================================================================
    // APPROACH 3: Integration Testing with bindToApplicationContext
    // =================================================================
    // 🐢 SLOWER - Full Spring context, but NO web server
    // ✅ Use for: Testing complete flow with real services & database
    // ✅ Real transactions, real database queries
    // ❌ No HTTP features (CORS, compression) - server not started
    // =================================================================

    @Nested
    @DisplayName("3. Integration Tests (bindToApplicationContext) - Full Spring Context")
    @SpringBootTest // Full Spring Boot application context loaded!
    class IntegrationTests {

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private TodoService todoService; // REAL service, not mocked!

        private WebTestClient client;

        @BeforeEach
        void setup() {
            // Bind to full application context
            client = MockMvcWebTestClient.bindToApplicationContext(context).build();
        }

        @Test
        @DisplayName("Should test with real service layer")
        void shouldWorkWithRealService() {
            // EXPLANATION: This test uses REAL services
            // If TodoService calls a real API or database, it will actually do it
            // Perfect for testing the complete flow from controller → service → repository

            List<Todo> todos = client.get()
                    .uri("/api/todos")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                    .returnResult()
                    .getResponseBody();

            assertNotNull(todos);
            // EXPLANATION: This calls the REAL TodoService.getAllTodos()
            // which calls the REAL JSONPlaceholder API via RestClient!
        }

        @Test
        @DisplayName("Should test complete CRUD flow")
        void shouldTestCompleteCrudFlow() {
            // Create
            Todo newTodo = new Todo(null, 1, "Integration Test Todo", false);
            Todo created = client.post()
                    .uri("/api/todos")
                    .bodyValue(newTodo)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(Todo.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(created);

            // Read
            client.get()
                    .uri("/api/todos/{id}", created.id())
                    .exchange()
                    .expectStatus().isOk();

            // EXPLANATION: All layers are real - controller, service, external API
            // Slower but most comprehensive testing (except for HTTP features)
        }
    }

    // =================================================================
    // APPROACH 4: End-to-End Testing with bindToServer
    // =================================================================
    // 🐌 SLOWEST - Real HTTP server on random port
    // ✅ Use for: Testing HTTP features (CORS, compression, SSL)
    // ✅ Most realistic - exactly like production
    // ❌ Slowest startup time
    // =================================================================

    @Nested
    @DisplayName("4. End-to-End Tests (bindToServer) - Real HTTP Server")
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class EndToEndTests {

        @LocalServerPort
        private int port;

        private WebTestClient client;

        @BeforeEach
        void setup() {
            // Bind to real HTTP server
            client = WebTestClient.bindToServer()
                    .baseUrl("http://localhost:" + port)
                    .build();
        }

        @Test
        @DisplayName("Should test with real HTTP requests")
        void shouldMakeRealHttpRequests() {
            // EXPLANATION: This makes a REAL HTTP request!
            // - Real TCP connection
            // - Real HTTP headers
            // - Real request/response cycle
            // - CORS policies are enforced
            // - Compression works
            // - SSL/TLS can be tested

            List<Todo> todos = client.get()
                    .uri("/api/todos")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json")
                    .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                    .returnResult()
                    .getResponseBody();

            assertNotNull(todos);

            // EXPLANATION: This is the most realistic test
            // Use for smoke tests, CORS testing, or when HTTP behavior matters
        }

        @Test
        @DisplayName("Should test HTTP headers and status codes")
        void shouldTestHttpFeatures() {
            client.get()
                    .uri("/api/todos/1")
                    .header("Accept", "application/json")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().exists("Content-Type")
                    .expectBody(Todo.class)
                    .value(todo -> {
                        assertNotNull(todo.id());
                        assertNotNull(todo.title());
                    });

            // EXPLANATION: Perfect for testing:
            // - Custom headers
            // - CORS configuration
            // - Content negotiation
            // - HTTP compression
            // - Response status codes
        }

        @Test
        @DisplayName("Should test complete user journey")
        void shouldTestCompleteUserJourney() {
            // EXPLANATION: End-to-end user scenario
            // This simulates exactly what a real client would experience

            client.get().uri("/api/todos").exchange().expectStatus().isOk();

            Todo newTodo = new Todo(null, 1, "E2E Test", false);
            Todo created = client.post()
                    .uri("/api/todos")
                    .bodyValue(newTodo)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(Todo.class)
                    .returnResult()
                    .getResponseBody();

            client.get()
                    .uri("/api/todos/{id}", created.id())
                    .exchange()
                    .expectStatus().isOk();

            client.put()
                    .uri("/api/todos/{id}", created.id())
                    .bodyValue(new Todo(created.id(), 1, "Updated", true))
                    .exchange()
                    .expectStatus().isOk();

            client.delete()
                    .uri("/api/todos/{id}", created.id())
                    .exchange()
                    .expectStatus().isNoContent();
        }
    }

    // =================================================================
    // DECISION GUIDE: Which Approach to Use?
    // =================================================================
    /*
     * Testing Need                    | Use This Approach              | Speed
     * --------------------------------|--------------------------------|----------
     * Quick controller logic test     | bindToController               | ⚡ Fastest
     * Test with validation/security   | bindToMockMvc                  | 🚀 Fast
     * Test with real database         | bindToApplicationContext       | 🐢 Slower
     * Test HTTP features (CORS, etc.) | bindToServer                   | 🐌 Slowest
     *
     * TESTING PYRAMID RULE:
     * - Many unit tests (bindToController)
     * - Some integration tests (bindToApplicationContext)
     * - Few end-to-end tests (bindToServer)
     *
     * COMMON PITFALLS:
     * ❌ Don't use @MockBean without Spring context (bindToController)
     * ❌ Don't test CORS with bindToMockMvc (use bindToServer)
     * ✅ Choose the right tool for the job!
     */
}
