package com.tecnocampus.LS2.protube_back.adapter.out.in_memory;

import com.tecnocampus.LS2.protube_back.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
