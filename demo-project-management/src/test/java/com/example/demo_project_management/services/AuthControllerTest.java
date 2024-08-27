package com.example.demo_project_management.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.demo_project_management.dto.AuthResponse;
import com.example.demo_project_management.dto.UserLoginDTO;
import com.example.demo_project_management.dto.UserRegisterDTO;
import com.example.demo_project_management.entity.Role;
import com.example.demo_project_management.entity.User;
import com.example.demo_project_management.enums.Roles;
import com.example.demo_project_management.repository.RoleRepository;
import com.example.demo_project_management.repository.UserRepository;
import com.example.demo_project_management.utility.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootTest
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserService userService;
//
//    @InjectMocks
//    private AuthController authController;

    @InjectMocks
    private AuthService authService;

    private UserRegisterDTO userRegisterDTO;
    private UserLoginDTO userLoginDTO;

    @BeforeEach
    void setUp() {
        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setName("Milan");
        userRegisterDTO.setEmail("milan5673@gmail.com");
        userRegisterDTO.setPassword("mahesh@122");
        userRegisterDTO.setRoles(List.of("DEVELOPER"));

        userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("milan5673@gmail.com");
        userLoginDTO.setPassword("mahesh@122");
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        Role developerRole = new Role();
        developerRole.setName(Roles.DEVELOPER);
        when(roleRepository.findByName(Roles.DEVELOPER)).thenReturn(developerRole);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded_password");

        User savedUser = new User();
        savedUser.setName("Milan");
        savedUser.setEmail("milan5673@gmail.com");
        savedUser.setPassword("encoded_password");
        savedUser.setRoles(Set.of(developerRole));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<?> responseEntity = userService.registerUser(userRegisterDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User responseUser = (User) responseEntity.getBody();
        assertEquals("Milan", responseUser.getName());
        assertEquals("milan5673@gmail.com", responseUser.getEmail());
        assertTrue(responseUser.getRoles().contains(developerRole));
    }

    @Test
    void testRegisterUser_Exception() {
        // Arrange
        when(roleRepository.findByName(any(Roles.class))).thenThrow(new RuntimeException("Role not found"));

        // Act
        ResponseEntity<?> responseEntity = userService.registerUser(userRegisterDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof RuntimeException);
    }

    @Test
    void testLogin_Success() {
        // Mocking
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANOTHER");
//        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
//        updatedAuthorities.add(authority);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userDetails.getUsername()).thenReturn("test@example.com");
//        when(userDetails.getAuthorities()).thenReturn(
//                Collections.singleton(new SimpleGrantedAuthority("DEVELOPER"))
//        );

//        when(jwtTokenUtil.generateToken(userDetails.getUsername())).thenReturn("mockToken");

        // Act
        ResponseEntity<?> responseEntity = userService.login(userLoginDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        AuthResponse authResponse = (AuthResponse) responseEntity.getBody();
//        assertNotNull(authResponse);
//        assertEquals("test@example.com", authResponse.getName());
//        assertEquals("mockToken", authResponse.getToken());
//        assertEquals(1, authResponse.getRoles().size());
//        assertEquals("ROLE_USER", authResponse.getRoles().get(0));
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act
        ResponseEntity<?> responseEntity = userService.login(userLoginDTO);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid username or password", responseEntity.getBody());
    }
}

