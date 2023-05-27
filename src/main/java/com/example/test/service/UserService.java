package com.example.test.service;

import com.example.test.dto.UserCreateRequestDto;
import com.example.test.model.entity.Role;
import com.example.test.model.entity.User;
import com.example.test.repository.RoleRepository;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public User save(UserCreateRequestDto userCreateRequestDto) {
        User user = new User();
        user.setLogin(userCreateRequestDto.getLogin());
        user.setPassword(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        Role role = roleRepository.findByName(userCreateRequestDto.getRole());
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

}
