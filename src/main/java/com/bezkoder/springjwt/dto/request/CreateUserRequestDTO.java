package com.bezkoder.springjwt.dto.request;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.enums.EGender;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDTO {
  @NotBlank(message = "username is required")
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank(message = "email is required")
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank(message = "fullName is required")
  @Size(min = 6, max = 50)
  private String fullName;

  private Set<String> role;

  @NotBlank(message = "password is required")
  @Size(min = 6, max = 40)
  private String password;

  @NotBlank(message = "gender is required")
  private String gender;

  @NotBlank(message = "phoneNumber is required")
  @Size(min = 10, max = 10, message = "phoneNumber must be 10 digits")
  private String phoneNumber;
 
  public User convertToEntity(CreateUserRequestDTO signupRequest) {
    User user = new User();
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setUsername(signupRequest.getUsername());
    user.setEmail(signupRequest.getEmail());
    user.setFullName(signupRequest.getFullName());
    user.setPassword(encoder.encode(signupRequest.getPassword()));
    user.setGender(EGender.valueOf(signupRequest.getGender()));
    user.setPhoneNumber(signupRequest.getPhoneNumber());
    return user;
  }
}
