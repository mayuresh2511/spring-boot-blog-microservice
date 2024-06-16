package com.microservices.user.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timeStamp,
                            String statusCode,
                            String message,
                            String details) {

}
