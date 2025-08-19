package com.dt.user_service.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int statusCode,
        String message,
        LocalDateTime timeStamp
) {}
