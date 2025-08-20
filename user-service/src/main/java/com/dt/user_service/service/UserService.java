package com.dt.user_service.service;

import com.dt.user_service.dto.UserRequest;
import com.dt.user_service.dto.UserResponse;

public interface UserService {
    void registerUser(UserRequest userRequest);

    UserResponse getUserByEmail(String email);
}
