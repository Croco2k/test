package com.example.test.controller;

import com.example.test.dto.UserCreateRequestDto;
import com.example.test.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

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


}
