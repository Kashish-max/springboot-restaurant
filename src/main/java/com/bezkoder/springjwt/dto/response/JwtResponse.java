package com.bezkoder.springjwt.dto.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private UUID id;
  private String username;
  private String email;
  private String fullName;
  private List<String> roles;
  private Date dateOfJoining;
  private String phoneNumber;

  public JwtResponse(String accessToken, UUID id, String username, String email, String fullName, List<String> roles, Date dateOfJoining, String phoneNumber) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.roles = roles;
    this.dateOfJoining = dateOfJoining;
    this.phoneNumber = phoneNumber;

  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }
}
