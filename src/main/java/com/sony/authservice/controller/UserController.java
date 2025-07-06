package com.sony.authservice.controller;

import com.sony.authservice.dto.request.LoginRequest;
import com.sony.authservice.dto.request.RegisterRequest;
import com.sony.authservice.dto.response.BaseResponse;
import com.sony.authservice.dto.response.user.UserResponse;
import com.sony.authservice.model.User;
import com.sony.authservice.service.UserService;

import com.sony.authservice.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request.getEmail(),request.getPassword());

        String token = jwtUtil.generateToken(user.getId(),null,null);

        return ResponseEntity.status(HttpStatus.CREATED).
                body(BaseResponse.success(new UserResponse(user,token),"Register success", HttpStatus.CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(),request.getPassword());

        String token = jwtUtil.generateToken(user.getId(),null,null);

        return ResponseEntity.ok(BaseResponse.success(new UserResponse(user,token),"Logic success", HttpStatus.OK));
    }
}
