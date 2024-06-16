package com.microservices.user.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String userRole;
    private final String subscriptionCategory;
    private final boolean isEmailVerified;
    private final boolean isMobileVerified;
    public CustomUserDetails(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             String userRole,
                             String subscriptionCategory,
                             boolean isEmailVerified,
                             boolean isMobileVerified) {
        super(username, password, authorities);
        this.userRole = userRole;
        this.subscriptionCategory = subscriptionCategory;
        this.isEmailVerified = isEmailVerified;
        this.isMobileVerified = isMobileVerified;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getSubscriptionCategory() {
        return subscriptionCategory;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }
}
