package com.sony.authservice.service.interfaces;

import com.sony.authservice.model.User;

public interface UserServiceInterface {
    User register(String email, String password);
    User getById(String id);
    User getByEmail(String email);
    User login(String email, String password);
}
