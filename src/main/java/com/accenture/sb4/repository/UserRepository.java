package com.accenture.sb4.repository;

import com.accenture.sb4.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return users;
    }

    public void save(User user) {
        users.add(user);
    }

    public User findById(Integer id) {
        return users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostConstruct
    private void init(){
        for (int i = 1; i <= 7; i++) {
            users.add(new User(i, "User" + i + " Lastname" + i, "user" + i + "@accenture.com"));
        }

    }
}
