package com.example.demo_project_management.controller;

import com.example.demo_project_management.dto.UserLoginDTO;
import com.example.demo_project_management.dto.UserRegisterDTO;
import com.example.demo_project_management.entity.User;
import com.example.demo_project_management.services.AuthService;
import com.example.demo_project_management.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

   @Autowired
   private UserService userService;

   @Autowired
   private AuthService authService;

   @PostMapping("/register")
   private ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO user) {
       return userService.registerUser(user);
   }


   @PostMapping("/login")
   private ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userCredentials) {
      return authService.login(userCredentials);
   }

}
