package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.dto.request.CreateUserRequestDTO;
import com.bezkoder.springjwt.dto.request.LoginRequestDTO;
import com.bezkoder.springjwt.dto.response.BaseResponse;
import com.bezkoder.springjwt.dto.response.JwtResponse;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
 
    @PostMapping("/signin")
    public ResponseEntity<BaseResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.singin(loginRequestDTO);
    }
 
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<String>> registerUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        return userService.signup(createUserRequestDTO);
    }
 
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers(HttpServletRequest req) {
        return userService.getAllUsers(req);
    }
 
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Optional<User>>> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
 
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }
}