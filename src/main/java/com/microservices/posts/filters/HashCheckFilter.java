package com.microservices.posts.filters;

import com.microservices.posts.utils.Utility;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class HashCheckFilter implements Filter {
    final private Logger logger = LoggerFactory.getLogger(HashCheckFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userName = request.getHeader("loggedInUser");
        String hashKey = request.getHeader("hashKey");
        logger.info("Username from request : " + userName + "hashKey from request : " + hashKey);
        if (userName == null || userName.isEmpty() || hashKey == null || hashKey.isEmpty()){
            System.out.println("Invalid Request Required Security Param Missing");
            throw new RuntimeException("Invalid Request Required Security Param Missing");
        }

        String generatedHashValue = Utility.generateHash(userName);
        logger.info("Username from generatedHashValue : " + generatedHashValue);
        if (!generatedHashValue.equals(hashKey)){
            System.out.println("Security Param Mismatch !!!!");
            throw new RuntimeException("Security Param Mismatch");
        }

        filterChain.doFilter(request, response);
    }
}
