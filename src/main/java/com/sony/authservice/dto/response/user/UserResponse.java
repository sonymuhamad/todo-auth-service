package com.sony.authservice.dto.response.user;

import com.sony.authservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String token;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public UserResponse(User user, String token) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.token = token;
    }
}
