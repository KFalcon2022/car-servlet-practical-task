package com.walking.carpractice.service;

import com.walking.carpractice.domain.User;
import com.walking.carpractice.exception.AuthException;
import com.walking.carpractice.exception.DuplicateUserException;
import com.walking.carpractice.repository.UserRepository;

public class UserService {
    private final EncodingService encodingService;

    private final UserRepository userRepository;

    public UserService(EncodingService encodingService, UserRepository userRepository) {
        this.encodingService = encodingService;
        this.userRepository = userRepository;
    }

    public User auth(String username, String password) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(AuthException::new);

        if (!encodingService.match(password, user.getPassword())) {
            throw new AuthException();
        }

        return user;
    }

    public User create(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new DuplicateUserException();
                });

        var encodedPassword = encodingService.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.create(user);
    }
}
