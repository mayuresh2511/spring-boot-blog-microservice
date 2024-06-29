package com.microservices.user.dto.awsSns;

public record AwsSnsOtpEventDto(String userName, String emailId, String otp) {
}
