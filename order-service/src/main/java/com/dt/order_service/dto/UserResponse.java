package com.dt.order_service.dto;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {}
