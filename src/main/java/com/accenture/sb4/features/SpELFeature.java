package com.accenture.sb4.features;


import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot 4 Feature: Improved Optional Support in SpEL
 * <p>
 * SpEL now handles Optional types natively with:
 * - Null-safe navigation (?.)
 * - Elvis operator (?:)
 * - Direct Optional methods (orElse, orElseGet, map)
 */

@Service
class UserService {
    private final Map<Long, String> users = Map.of(
            1L, "John Doe",
            2L, "Jane Smith",
            3L, "Bob Johnson"
    );

    public Optional<String> findUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<Integer> getUserAge(Long id) {
        return Optional.of(30);
    }
}

@Configuration
class SpELConfig {

    // SpEL with Optional.orElse()
    @Bean
    String defaultUserName(
            @Value("#{userService.findUser(999L).orElse('Guest')}") String name
    ) {
        return name;
    }

}

@RestController
class SpELController {

    // 1. Using Optional.orElse() in SpEL
    @Value("#{userService.findUser(1L).orElse('Unknown User')}")
    private String user1;

    // 2. Using Elvis operator with Optional
    @Value("#{userService.findUser(999L).orElse(null) ?: 'Default Guest'}")
    private String guestUser;

    // 3. Optional with isPresent and get (Spring Boot 4 way)
    @Value("#{userService.findUser(2L).isPresent() ? userService.findUser(2L).get().toUpperCase() : 'NO USER'}")
    private String user2Upper;

    // 4. Complex SpEL with Optional chaining
    @Value("#{userService.findUser(3L).isPresent() ? userService.findUser(3L).get() : 'Not Found'}")
    private String user3;

    // 5. Simple default value approach
    @Value("#{userService.findUser(1L).orElse('Admin')}")
    private String configUser;

    private final UserService userService;

    public SpELController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/spel/demo")
    public Map<String, Object> demonstrateSpEL() {
        return Map.of(
                "user1", user1,
                "guestUser", guestUser,
                "user2Upper", user2Upper,
                "user3", user3,
                "configUser", configUser
        );
    }

    // Dynamic SpEL evaluation at runtime
    @GetMapping("/api/spel/user/{id}")
    public Map<String, Object> getUserWithSpEL(@PathVariable Long id) {
        Optional<String> user = userService.findUser(id);
        Optional<Integer> age = userService.getUserAge(id);

        return Map.of(
                "userId", id,
                "userName", user.orElse("Guest"),
                "userAge", age.orElse(0),
                "hasUser", user.isPresent(),
                "userUpperCase", user.map(String::toUpperCase).orElse("N/A"),
                "welcomeMessage", user.map(name -> "Welcome, " + name + "!")
                        .orElse("Welcome, Guest!")
        );
    }
}