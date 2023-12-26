package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.dto.request.CreateUserRequestDTO;
import com.bezkoder.springjwt.dto.request.LoginRequestDTO;
import com.bezkoder.springjwt.dto.response.BaseResponse;
import com.bezkoder.springjwt.dto.response.JwtResponse;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.enums.ERole;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.helpers.GlobalUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
 
@Slf4j
@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;
 
    @Autowired
    JwtUtils jwtUtils;
 
    @Autowired
    UserRepository userRepository;
 
    @Autowired
    RoleRepository roleRepository;
 
    @Autowired
    GlobalUtils globalUtils;
 
    public ResponseEntity<BaseResponse<JwtResponse>> singin(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
 
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
 
        return ResponseEntity.ok().body(BaseResponse.success(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFullName(),
                roles,
                userDetails.getDateOfJoining(),
                userDetails.getPhoneNumber()
        )));
    }
 
    public ResponseEntity<BaseResponse<String>> signup(CreateUserRequestDTO createUserRequestDTO) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(createUserRequestDTO.getUsername()))) {
            log.error("Logging - > Error: Username is already taken!");
            return ResponseEntity
                    .badRequest()
                    .body(BaseResponse.failure("Error: Username is already taken!"));
        }
 
        if (Boolean.TRUE.equals(userRepository.existsByEmail(createUserRequestDTO.getEmail()))) {
            log.error("Logging - > Error: Email is already in use!");
            return ResponseEntity
                    .badRequest()
                    .body(BaseResponse.failure("Error: Email is already in use!"));
        }
 
        User user = createUserRequestDTO.convertToEntity(createUserRequestDTO);
        Set<String> strRoles = createUserRequestDTO.getRole();
        if (strRoles == null || strRoles.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(BaseResponse.failure("Error: Roles field cannot be null"));
        }
 
        Set<Role> roles = GlobalUtils.getEnumeratedListOfRoles(strRoles, roleRepository);
        user.setRoles(roles);
 
        if (roles.stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
            return new ResponseEntity<>(BaseResponse.failure("You are not allowed to assign yourself with ADMIN Role. Possible Roles: [CUSTOMER, MANAGER]"), HttpStatus.UNAUTHORIZED);
        }
 
        userRepository.save(user);
        return new ResponseEntity<>(BaseResponse.success("User created Successfully"), HttpStatus.CREATED);
    }
 
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers(HttpServletRequest req) {
        List<User> allUsers = userRepository.findAll();
        return new ResponseEntity<>(BaseResponse.success(allUsers), HttpStatus.OK);
    }
 
    public ResponseEntity<BaseResponse<Optional<User>>> getUserById(String id) {
        UUID uuidId = UUID.fromString(id);
        if (!userRepository.existsById(uuidId)) {
            return new ResponseEntity<>(BaseResponse.failure("User does not exist with this id"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(BaseResponse.success(userRepository.findById(UUID.fromString(id))), HttpStatus.OK);
    }
 
    public ResponseEntity<BaseResponse<String>> deleteUserById(String id) {
        UUID uuidId = UUID.fromString(id);
        if (!userRepository.existsById(uuidId)) {
            return new ResponseEntity<>(BaseResponse.failure("User does not exist with this id"), HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(uuidId);
        return new ResponseEntity<>(BaseResponse.success("User removed successfully"), HttpStatus.OK);
    }
 
}
