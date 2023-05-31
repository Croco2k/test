package com.example.test.service;

import com.example.test.dto.UserCreateRequestDto;
import com.example.test.model.entity.Role;
import com.example.test.model.entity.User;
import com.example.test.model.enums.RoleName;
import com.example.test.repository.RoleRepository;
import com.example.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    public void testSave() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto("testuser", "password", RoleName.ADMIN);
        Role role = new Role(RoleName.ADMIN);
        when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(role);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        User savedUser = userService.save(userCreateRequestDto);

        assertEquals(userCreateRequestDto.getLogin(), savedUser.getLogin());
        assertEquals(userCreateRequestDto.getRole(), savedUser.getRole().getName());
    }
}