package com.microservices.user.model;

public class AuthenticationResponse extends BaseResponseImpl{

  private final String username;
  private final boolean isAuthSuccessful;
  private final String jwtAccessToken;
  private final String jwtRefreshToken;

  public AuthenticationResponse(String username,
                                boolean isAuthSuccessful,
                                String jwtAccessToken,
                                String jwtRefreshToken,
                                int statusCode,
                                String message) {
    super(statusCode, message);
    this.username = username;
    this.isAuthSuccessful = isAuthSuccessful;
    this.jwtAccessToken = jwtAccessToken;
    this.jwtRefreshToken = jwtRefreshToken;
  }

  public String getUsername() {
    return username;
  }

  public boolean isAuthSuccessful() {
    return isAuthSuccessful;
  }

  public String getJwtAccessToken() {
    return jwtAccessToken;
  }

  public String getJwtRefreshToken() {
    return jwtRefreshToken;
  }

  @Override
  public String toString() {
    return "AuthenticationResponse{" +
            "username='" + username + '\'' +
            ", isAuthSuccessful=" + isAuthSuccessful +
            ", jwtAccessToken='" + jwtAccessToken + '\'' +
            ", jwtRefreshToken='" + jwtRefreshToken + '\'' +
            '}';
  }
}
