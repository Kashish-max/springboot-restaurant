package com.bezkoder.springjwt.helpers;

 
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.enums.ERole;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtAuthTokenFilter;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
 
@Service
public class GlobalUtils {
    private static final String ROLE_NOT_FOUND = "Error: Role is not found";
 
    @Autowired
    UserRepository userRepository;
 
    @Autowired
    JwtUtils jwtUtils;
 
    public static Set<Role> getEnumeratedListOfRoles(Set<String> strRoles, RoleRepository roleRepository) {
        Set<Role> roles = new HashSet<>();
 
        strRoles.forEach(role -> {
            switch (role) {
                case "ADMIN":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                    roles.add(adminRole);
 
                    break;
                case "MANAGER":
                    Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                            .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                    roles.add(modRole);
 
                    break;
                case "CUSTOMER":
                    Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                            .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                    roles.add(userRole);
                    break;
                default:
                    throw new RuntimeException(ROLE_NOT_FOUND);
            }
        });
 
        return roles;
    }
 
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(dateString);
            return new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date", e);
        }
    }
 
    public User getCurrentUser(HttpServletRequest req) {
        String jwt = JwtAuthTokenFilter.parseJwt(req);
        String currentUserUsername = jwtUtils.getUserNameFromJwtToken(jwt);
        return userRepository.findByUsername(currentUserUsername).orElseThrow(() -> new RuntimeException("User not found!"));
    }
}
