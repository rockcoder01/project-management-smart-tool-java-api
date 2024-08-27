package com.example.demo_project_management.services;

import com.example.demo_project_management.dto.UserLoginDTO;
import com.example.demo_project_management.dto.UserRegisterDTO;
import com.example.demo_project_management.entity.Role;
import com.example.demo_project_management.entity.User;
import com.example.demo_project_management.enums.Roles;
import com.example.demo_project_management.repository.RoleRepository;
import com.example.demo_project_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder passwordEncoder;


    @Transactional
    public ResponseEntity<?> registerUser(UserRegisterDTO userRequest) {
        try {
            User user = new User();
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

            Set<Role> roles = new HashSet<>();
            for (String roleItem : userRequest.getRoles()) {
                Roles roleEnum = Roles.valueOf(roleItem);
                Role role = roleRepository.findByName(roleEnum);
                roles.add(role);
            }
            user.setRoles(roles);
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> login(UserLoginDTO userCredentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentials.getEmail(),
                            userCredentials.getPassword()
                    )
            );

            // If authentication is successful, you can return a token or user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Generate and return JWT token or other response here
            return ResponseEntity.ok(userDetails);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}

