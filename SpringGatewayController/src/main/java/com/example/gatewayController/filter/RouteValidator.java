package com.example.gatewayController.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/user/api/v1/auth",
            "/user/api/v1/refresh",
            "/user/api/v1/register",
            "eureka"
    );

    public Predicate<ServerHttpRequest> isAccessTokenRequired =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
