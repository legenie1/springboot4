package com.accenture.sb4.controller;

import com.accenture.sb4.dto.UserDTOv1;
import com.accenture.sb4.dto.UserDTOv2;
import com.accenture.sb4.entity.User;
import com.accenture.sb4.mapper.UserMapper;
import com.accenture.sb4.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    // ================= PATH SEGMENT VERSIONING =================
    @GetMapping(value = "/{version}/users", version = "1.0")
    public List<User> getUsersv1() {
        log.info("GET /api/v1/users - PATH SEGMENT VERSIONING: {}", "v1");
        return userRepository.findAll();
    }

    @GetMapping(value = "/{version}/users", version = "2.0")
    public List<User> getUsersV2() {
        log.info("GET /api/v2/users - PATH SEGMENT VERSIONING: {}", "v2");
        return userRepository.findAll();
    }

    // ================= REQUEST HEADER VERSIONING =================
    @GetMapping(value = "/users", version = "1.0")
    public List<UserDTOv1> findAllv1() {
        log.info("GET /api/v1/users - REQUEST HEADER VERSIONING: {}", "v1");
        return userRepository.findAll().stream().map(userMapper::toV1).toList();
    }

    @GetMapping(value = "/users", version = "2.0")
    public List<UserDTOv2> findAllv2() {
        log.info("GET /api/v2/users - REQUEST VERSIONING: {}", "v2");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV2).toList();
    }

    // ================= REQUEST PARAMETER VERSIONING =================
    @GetMapping(value = "/users/list", params = "version=1.0")
    public List<UserDTOv1> listUsersV1(@RequestParam String version) {
        log.info("GET /api/users/list?version=1.0 - REQUEST PARAMETER VERSIONING: {}", version);
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV1).toList();
    }

    @GetMapping(value = "/users/list", params = "version=v2")
    public List<UserDTOv2> listUsersV2(@RequestParam String version) {
        log.info("GET /api/users/list?version=2.0 - REQUEST PARAMETER VERSIONING: {}", version);
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV2).toList();
    }

    // ================= MEDIA TYPE VERSIONING =================
    @GetMapping(value = "/users/media", version = "1.0", produces = "application/json")
    public List<UserDTOv1> getUsersMediaV1() {
        log.info("Find All Users using media type versioning: {}", "v1");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV1).toList();
    }

    @GetMapping(value = "/users/media", version = "2.0", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTOv2> getUsersMediaV2() {
        log.info("Find All Users using media type versioning: {}", "v2");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV2).toList();
    }

    public @NonNull String getUserName(@Nullable User user) {
        if (user == null) {
            return "Unknown User";
        }
        return user.name();
    }
}
