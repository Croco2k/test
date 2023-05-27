package com.example.test.controller;

import com.example.test.dto.UserCreateRequestDto;
import com.example.test.model.entity.Role;
import com.example.test.model.entity.User;
import com.example.test.repository.RoleRepository;
import com.example.test.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto("testuser", "testpassword", "TEST_ROLE");
        Role role = new Role(1L, "TEST_ROLE");
        roleRepository.save(role);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDto)))
                .andExpect(status().isCreated());

        User savedUser = userRepository.findByLogin(userCreateRequestDto.getLogin());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getLogin()).isEqualTo(userCreateRequestDto.getLogin());
        assertThat(savedUser.getRole().getName()).isEqualTo(userCreateRequestDto.getRole());
        assertThat(passwordEncoder.matches(userCreateRequestDto.getPassword(), savedUser.getPassword())).isTrue();
    }
}