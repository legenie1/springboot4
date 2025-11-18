package com.accenture.sb4.controller;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NullMarked
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final List<Product> products = new ArrayList<>();
    private Long idCounter = 1L;

    @GetMapping
    public List<Product> getAllProducts() {
        return List.copyOf(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateRequest request) {
        Product product = new Product(
                idCounter++,
                request.name,
                request.price,
                request.description  // Can be null
        );
        products.add(product);
        return product;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateRequest request) {

        return findById(id).map(existing -> {
            products.remove(existing);
            Product updated = new Product(
                    id,
                    request.name != null ? request.name : existing.name,
                    request.price != null ? request.price : existing.price,
                    request.description != null ? request.description : existing.description
            );
            products.add(updated);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean removed = products.removeIf(p -> p.id.equals(id));
        return removed ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Search by name - can return null
    @GetMapping("/search")
    @Nullable
    public Product searchByName(@RequestParam String name) {
        return products.stream()
                .filter(p -> p.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.id.equals(id))
                .findFirst();
    }

    record Product(
            Long id,
            String name,
            Double price,
            @Nullable String description
    ) {}


    record CreateRequest(String name, Double price, @Nullable String description) {}
    record UpdateRequest(@Nullable String name, @Nullable Double price, @Nullable String description) {}
}