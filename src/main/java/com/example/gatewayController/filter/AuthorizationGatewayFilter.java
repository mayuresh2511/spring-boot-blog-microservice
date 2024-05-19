package com.example.gatewayController.filter;

import com.example.gatewayController.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationGatewayFilter extends AbstractGatewayFilterFactory<AuthorizationGatewayFilter.Config> {

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
                System.out.println("Auth filter is on");

                if (validator.isSecured.test(exchange.getRequest())){

                    if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                        System.out.println("Auth token not passed");
                        throw new RuntimeException("Auth token not passed");
                    }

                    ServerHttpRequest request;
                    String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    if (token != null && token.startsWith("Bearer ")){
                        token = token.substring(7);
                    }

                    boolean isValidToken = jwtUtils.validateToken(token);

                    if (!isValidToken){
                        System.out.println("Token is not valid");
                        throw new RuntimeException("Token is not valid");
                    }

                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", jwtUtils.extractUsername(token))
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                }else{
                    return chain.filter(exchange);
                }
            }else {
                System.out.println("Auth filter has been turned off");
                return chain.filter(exchange);
            }
        });
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
