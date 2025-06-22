package com.sony.authservice.service;

import com.sony.authservice.exception.BadRequestException;
import com.sony.authservice.exception.DuplicateException;
import com.sony.authservice.exception.NotFoundException;
import com.sony.authservice.model.User;
import com.sony.authservice.repository.UserRepository;
import com.sony.authservice.service.interfaces.UserServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User register(String email, String password) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new DuplicateException("User with such email already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);

        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User login(String email,String password) {
        Optional<User> existing = Optional.of(userRepository.findByEmail(email).
                orElseThrow(() -> new NotFoundException("User with email " + email + " not found")));

        if (!passwordEncoder.matches(password, existing.get().getPassword())){
            throw new BadRequestException("Wrong password");
        }

        return existing.get();
    }
}