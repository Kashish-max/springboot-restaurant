package com.bezkoder.springjwt.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.enums.EGender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
 
import java.util.*;
import java.util.stream.Collectors;
 
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private final UUID id;
 
    private final String email;
 
    private final String username;
 
    private final String fullName;
 
    @JsonIgnore
    private final String password;
 
    private final Collection<? extends GrantedAuthority> authorities;
 
    private final Date dateOfJoining;
 
    private final EGender gender;
 
    private final String phoneNumber;
 
    public UserDetailsImpl(UUID id, String username, String email, String fullName, String password,
                           Collection<? extends GrantedAuthority> authorities, Date dateOfJoining, EGender gender, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.authorities = authorities;
        this.dateOfJoining = dateOfJoining;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
 
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
 
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPassword(),
                authorities,
                user.getDateOfJoining(),
                user.getGender(),
                user.getPhoneNumber()
        );
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
 
    @Override
    public String getPassword() {
        return password;
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        return true;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
