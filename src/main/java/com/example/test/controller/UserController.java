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
    public String admin() {
        String response = "admin";
        System.out.println(("admin() response: " + response));
        return response;
    }

    @GetMapping("/moderator")
    public String moderator() {
        return "moderator";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/anonymous")
    public String anonymous() {
        return "anonymous";
    }


}
