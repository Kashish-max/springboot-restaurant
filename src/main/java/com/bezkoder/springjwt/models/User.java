package com.bezkoder.springjwt.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.bezkoder.springjwt.models.enums.EGender;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Column(name = "full_name")
  @Size(max = 50)
  private String fullName;

  @NotBlank
  @Size(max = 120)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @Column(name = "date_of_jpoining")
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateOfJoining;

  @Enumerated(EnumType.STRING)
  @Column(name="gender")
  private EGender gender;

  @NotBlank
  @Size(max = 10, min = 10)
  private String phoneNumber;
  
  public User(String username, String email, String fullName, String password, EGender gender, String phoneNumber) {
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.password = password;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
  }
}
