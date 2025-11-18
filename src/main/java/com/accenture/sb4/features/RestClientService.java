package com.accenture.sb4.features;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Spring Boot 4 Feature: RestClient (New HTTP Client)
 *
 * RestClient replaces RestTemplate with:
 * - Fluent API
 * - Better error handling
 * - Virtual thread support
 * - Type-safe responses
 */

//@Configuration
//class RestClientConfig {
//
//    @Bean
//    RestClient restClient(RestClient.Builder builder) {
//        return builder
//                .baseUrl("https://jsonplaceholder.typicode.com")
//                .defaultHeader("User-Agent", "Spring-Boot-4-Demo")
//                .build();
//    }
//}

@Service
class ApiClientService {

    RestClient restClient = RestClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .defaultHeader("User-Agent", "Spring-Boot-4-Demo")
            .build();

    // Simple GET request
    public Post getPost(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Post.class);
    }

    // GET with error handling
    public Post getPostWithErrorHandling(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("Client error: " + response.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Server error: " + response.getStatusCode());
                })
                .body(Post.class);
    }

    // GET list of items
    public List<Post> getAllPosts() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<Post>>() {});
    }

    // POST request
    public Post createPost(Post post) {
        return restClient.post()
                .uri("/posts")
                .body(post)
                .retrieve()
                .body(Post.class);
    }

    // PUT request
    public Post updatePost(Long id, Post post) {
        return restClient.put()
                .uri("/posts/{id}", id)
                .body(post)
                .retrieve()
                .body(Post.class);
    }

    // DELETE request
    public void deletePost(Long id) {
        restClient.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    // GET with ResponseEntity
    public ResponseEntity<Post> getPostWithHeaders(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .toEntity(Post.class);
    }

    // Custom headers
    public Post getPostWithCustomHeaders(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .header("X-Custom-Header", "MyValue")
                .header("Authorization", "Bearer token123")
                .retrieve()
                .body(Post.class);
    }
}

@RestController
class RestClientController {

    private final ApiClientService apiClient;

    public RestClientController(ApiClientService apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/api/restclient/post/{id}")
    public Post getPost(@PathVariable Long id) {
        return apiClient.getPost(id);
    }

    @GetMapping("/api/restclient/posts")
    public List<Post> getAllPosts() {
        return apiClient.getAllPosts();
    }

    @GetMapping("/api/restclient/post/{id}/with-headers")
    public ResponseEntity<Post> getPostWithHeaders(@PathVariable Long id) {
        return apiClient.getPostWithHeaders(id);
    }

    @GetMapping("/api/restclient/demo")
    public Map<String, Object> demonstrateRestClient() {
        Post post = apiClient.getPost(1L);
        List<Post> allPosts = apiClient.getAllPosts();

        return Map.of(
                "singlePost", post,
                "totalPosts", allPosts.size(),
                "firstThreePosts", allPosts.subList(0, Math.min(3, allPosts.size()))
        );
    }
}

// DTO
record Post(Long id, Long userId, String title, String body) {}