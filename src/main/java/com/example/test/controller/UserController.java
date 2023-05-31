package com.example.test.controller;

import com.example.test.dto.UserCreateRequestDto;
import com.example.test.jwt.dto.JwtRequest;
import com.example.test.jwt.dto.JwtResponse;
import com.example.test.jwt.util.JwtUtils;
import com.example.test.model.entity.User;
import com.example.test.service.UserDetailsServiceImpl;
import com.example.test.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getLogin(), authenticationRequest.getPassword());
            final User user = userDetailsService.loadUserByUsername(authenticationRequest.getLogin());
            final String token = jwtUtils.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        userService.save(userCreateRequestDto);
        return new ResponseEntity<>(userCreateRequestDto.getLogin(), HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/moderator")
    public ResponseEntity<String> moderator() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<String> user() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/anonymous")
    public ResponseEntity<String> anonymous() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void authenticate(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
    }

}
