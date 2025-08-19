package com.dt.user_service.service;

import com.dt.user_service.dto.UserRequest;

public interface UserService {
    void registerUser(UserRequest userRequest);
}
