package com.example.test.dto;

import com.example.test.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    private String login;
    private String password;
    private RoleName role;

//    public UserCreateRequestDto(JsonObject json) {
//        this.login = json.get("login").getAsString();
//        this.password = json.get("password").getAsString();
//        this.role = RoleName.valueOf(json.get("role").getAsString());
//    }
}
