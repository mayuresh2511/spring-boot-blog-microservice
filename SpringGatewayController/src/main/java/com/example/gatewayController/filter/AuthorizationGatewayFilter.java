package com.example.gatewayController.filter;

import com.example.gatewayController.util.JwtUtils;
import com.example.gatewayController.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthorizationGatewayFilter extends AbstractGatewayFilterFactory<AuthorizationGatewayFilter.Config> {
    final private Logger logger = LoggerFactory.getLogger(AuthorizationGatewayFilter.class);
    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtils jwtUtils;

    public AuthorizationGatewayFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config){
        return ((exchange, chain) -> {

            if (config.isPreFilter()){
                logger.info("Auth filter is on");
                ServerHttpRequest request;
                if (validator.isAccessTokenRequired.test(exchange.getRequest())){

                    String token = extractToken(exchange);

                    if (!jwtUtils.extractTokenType(token).equalsIgnoreCase("access_token")){
                        logger.info("Wrong token type");
                        throw new RuntimeException("Token type is incorrect please use access token");
                    }

                    String userName = jwtUtils.extractUsername(token);
                    String userRole = jwtUtils.extractClaim(token, "USER_ROLE");
                    String userSubscription = jwtUtils.extractClaim(token, "SUBSCRIPTION_CATEGORY");

                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", userName)
                            .header("hashKey", Utility.generateHash(userName))
                            .header("USER_ROLE", userRole)
                            .header("SUBSCRIPTION_CATEGORY", userSubscription)
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                } else if (exchange.getRequest().getURI().getPath().contains("/user/api/v1/refresh")) {
                    String token = extractToken(exchange);

                    if (!jwtUtils.extractTokenType(token).equalsIgnoreCase("refresh_token")){
                        logger.info("Wrong token type");
                        throw new RuntimeException("Token type is incorrect please use refresh token");
                    }

                    String userName = jwtUtils.extractUsername(token);

                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", userName)
                            .header("hashKey", Utility.generateHash(userName))
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                } else{
                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", "Guest")
                            .header("hashKey", Utility.generateHash("Guest"))
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                }
            }else {
                logger.info("Auth filter has been turned off");
                return chain.filter(exchange);
            }
        });
    }

    private String extractToken(ServerWebExchange exchange) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            logger.info("Auth token not passed");
            throw new RuntimeException("Auth token not passed");
        }

        String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        logger.info("JWT Token : " + token);
        boolean isValidToken = jwtUtils.validateToken(token);

        if (!isValidToken){
            logger.info("Token is not valid");
            throw new RuntimeException("Token is not valid");
        }
        return token;
    }

    public static class Config{
        private boolean preFilter;
        private boolean postFilter;

        public Config() {}

        public Config(boolean preFilter, boolean postFilter) {
            this.preFilter = preFilter;
            this.postFilter = postFilter;
        }

        public boolean isPreFilter() {
            return preFilter;
        }

        public void setPreFilter(boolean preFilter) {
            this.preFilter = preFilter;
        }

        public boolean isPostFilter() {
            return postFilter;
        }

        public void setPostFilter(boolean postFilter) {
            this.postFilter = postFilter;
        }
    }
}
