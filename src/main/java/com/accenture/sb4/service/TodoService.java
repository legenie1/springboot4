package com.accenture.sb4.service;

import com.accenture.sb4.entity.Todo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service

public class TodoService {
    RestClient restClient = RestClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build();

    public List<Todo> getAllTodos() {
        return restClient.get()
                .uri("/todos")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<Todo>>() {});
    }

    public List<Todo> getTodosByUserId(Integer userId) {
        return restClient.get()
                .uri("/todos?userId={userId}", userId)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<Todo>>() {});
    }

    public Todo getTodoById(Integer id) {
        return restClient.get()
                .uri("/todos/{id}", id)
                .retrieve()
                .body(Todo.class);
    }

    public Todo createTodo(Todo todo) {
        return restClient.post()
                .uri("/todos")
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public Todo updateTodo(Integer id, Todo todo) {
        return restClient.put()
                .uri("/todos/{id}", id)
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public Todo patchTodo(Integer id, Todo todo) {
        return restClient.patch()
                .uri("/todos/{id}", id)
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public void deleteTodo(Integer id) {
        restClient.delete()
                .uri("/todos/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    public List<Todo> getCompletedTodos() {
        return restClient.get()
                .uri("/todos?completed=true")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<Todo>>() {});
    }

    public List<Todo> getIncompleteTodos() {
        return restClient.get()
                .uri("/todos?completed=false")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<Todo>>() {});
    }
}
