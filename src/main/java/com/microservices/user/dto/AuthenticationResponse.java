package com.microservices.user.dto;

public class AuthenticationResponse {

  private String username;
  private boolean isAuthSuccessful;
  private String jwtToken;
  private String authMessage;

  public AuthenticationResponse(String username, boolean isAuthSuccessful, String jwtToken, String authMessage) {
    this.username = username;
    this.isAuthSuccessful = isAuthSuccessful;
    this.jwtToken = jwtToken;
    this.authMessage = authMessage;
  }

  public AuthenticationResponse() {

  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isAuthSuccessful() {
    return isAuthSuccessful;
  }

  public void setAuthSuccessful(boolean authSuccessful) {
    isAuthSuccessful = authSuccessful;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getAuthMessage() {
    return authMessage;
  }

  public void setAuthMessage(String authMessage) {
    this.authMessage = authMessage;
  }

  @Override
  public String toString() {
    return "AuthenticationResponse{" +
            "username='" + username + '\'' +
            ", isAuthSuccessful=" + isAuthSuccessful +
            ", jwtToken='" + jwtToken + '\'' +
            ", authMessage='" + authMessage + '\'' +
            '}';
  }
}
