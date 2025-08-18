package com.dt.product_catalog_service.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int statusCode,
        String message,
        LocalDateTime timeStamp
) {}
