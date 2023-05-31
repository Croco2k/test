package com.example.test.controller;

import com.example.test.dto.UserCreateRequestDto;
import com.example.test.jwt.util.JwtUtils;
import com.example.test.model.entity.Role;
import com.example.test.model.entity.User;
import com.example.test.model.enums.RoleName;
import com.example.test.repository.RoleRepository;
import com.example.test.repository.UserRepository;
import com.example.test.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class UserControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

//    @Test
//    public void testCreateUser() {
//        Role role = new Role(RoleName.ADMIN);
//        roleRepository.save(role);
//
//        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto("testuser", "password", RoleName.ADMIN);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<UserCreateRequestDto> request = new HttpEntity<>(userCreateRequestDto, headers);
//
//        ResponseEntity<Void> response = restTemplate.postForEntity("/api/users", request, Void.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        User savedUser = userRepository.findByLogin(userCreateRequestDto.getLogin());
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getLogin()).isEqualTo(userCreateRequestDto.getLogin());
//        assertThat(savedUser.getRole().getName().getValue()).isEqualTo(userCreateRequestDto.getRole().getValue());
//        assertThat(passwordEncoder.matches(userCreateRequestDto.getPassword(), savedUser.getPassword())).isTrue();
//    }

    @Test
    public void testAdmin() {
        Role role = new Role(RoleName.ADMIN);
        roleRepository.save(role);

        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto("admin", "admin", RoleName.ADMIN);
        userService.save(userCreateRequestDto);

        String token = "test";

        when(jwtUtils.getLoginFromToken(eq(token))).thenReturn(userCreateRequestDto.getLogin());
        when(jwtUtils.getRoleFromToken(eq(token))).thenReturn(userCreateRequestDto.getRole());
        when(jwtUtils.validateToken(eq(token), any(User.class))).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> responseAdmin = restTemplate.exchange("/api/admin", HttpMethod.GET, request, Void.class);
        assertThat(responseAdmin.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Void> responseModerator = restTemplate.exchange("/api/moderator", HttpMethod.GET, request, Void.class);
        assertThat(responseModerator.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Void> responseUser = restTemplate.exchange("/api/user", HttpMethod.GET, request, Void.class);
        assertThat(responseUser.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testModerator() {
        Role role = new Role(RoleName.MODERATOR);
        roleRepository.save(role);

        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto("moderator", "moderator", RoleName.MODERATOR);
        userService.save(userCreateRequestDto);

        String token = "test";

        when(jwtUtils.getLoginFromToken(eq(token))).thenReturn(userCreateRequestDto.getLogin());
        when(jwtUtils.getRoleFromToken(eq(token))).thenReturn(userCreateRequestDto.getRole());
        when(jwtUtils.validateToken(eq(token), any(User.class))).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> responseAdmin = restTemplate.exchange("/api/admin", HttpMethod.GET, request, Void.class);
        assertThat(responseAdmin.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ResponseEntity<Void> responseModerator = restTemplate.exchange("/api/moderator", HttpMethod.GET, request, Void.class);
        assertThat(responseModerator.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Void> responseUser = restTemplate.exchange("/api/user", HttpMethod.GET, request, Void.class);
        assertThat(responseUser.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testUser() {
        Role role = new Role(RoleName.USER);
        roleRepository.save(role);

        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto("user", "user", RoleName.USER);
        userService.save(userCreateRequestDto);

        String token = "test";

        when(jwtUtils.getLoginFromToken(eq(token))).thenReturn(userCreateRequestDto.getLogin());
        when(jwtUtils.getRoleFromToken(eq(token))).thenReturn(userCreateRequestDto.getRole());
        when(jwtUtils.validateToken(eq(token), any(User.class))).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> responseAdmin = restTemplate.exchange("/api/admin", HttpMethod.GET, request, Void.class);
        assertThat(responseAdmin.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ResponseEntity<Void> responseModerator = restTemplate.exchange("/api/moderator", HttpMethod.GET, request, Void.class);
        assertThat(responseModerator.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ResponseEntity<Void> responseUser = restTemplate.exchange("/api/user", HttpMethod.GET, request, Void.class);
        assertThat(responseUser.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testAnonymous() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/anonymous", HttpMethod.GET, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Void> responseAdmin = restTemplate.exchange("/api/admin", HttpMethod.GET, request, Void.class);
        assertThat(responseAdmin.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ResponseEntity<Void> responseModerator = restTemplate.exchange("/api/moderator", HttpMethod.GET, request, Void.class);
        assertThat(responseModerator.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ResponseEntity<Void> responseUser = restTemplate.exchange("/api/user", HttpMethod.GET, request, Void.class);
        assertThat(responseUser.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}