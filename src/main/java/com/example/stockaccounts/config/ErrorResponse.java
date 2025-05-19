package com.example.stockaccounts.config;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        String ExceptionType,
        LocalDateTime timeStamp
) {
}
