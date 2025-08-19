package com.dt.user_service.dto;

public record UserRequest (
        String firstName,
        String lastName,
        String email,
        String password
){}
