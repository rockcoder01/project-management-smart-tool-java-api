package com.example.demo_project_management.services;
import com.example.demo_project_management.dto.AuthResponse;
import com.example.demo_project_management.dto.UserLoginDTO;
import com.example.demo_project_management.entity.User;
import com.example.demo_project_management.repository.UserRepository;
import com.example.demo_project_management.utility.JwtTokenUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(UserRepository userRepository,@Lazy AuthenticationManager authenticationManager,@Lazy PasswordEncoder passwordEncoder,@Lazy JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user by email from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert user roles to GrantedAuthority collection
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // Create UserDetails object and return
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

        public ResponseEntity<?> login(UserLoginDTO userCredentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentials.getEmail(),
                            userCredentials.getPassword()
                    )
            );

            UserDetails userDetails1 = this.loadUserByUsername(userCredentials.getEmail());

            // Generate the token
            String token = jwtTokenUtil.generateToken(userDetails1.getUsername());

            // Get user details from the authenticated principal
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            //Extract roles

            // Extract user roles and convert to List
            List<String> roles = new ArrayList<>(userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet()));

            // Return the token in the response
            AuthResponse response = new AuthResponse();
            response.setName(userDetails1.getUsername());
            response.setRoles(roles);
            response.setToken(token);

            // Handle successful authentication (e.g., return a JWT)
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid123 username or password," + e.getMessage());
        }
    }
}

