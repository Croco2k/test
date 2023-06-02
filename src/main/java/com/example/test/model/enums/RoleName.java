package com.example.test.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName {
    ADMIN("ADMIN"),
    MODERATOR("MODERATOR"),
    USER("USER");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RoleName fromValue(String value) {
        for (var operation : RoleName.values()) {
            if (operation.value.equals(value)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Unexpected value " + value);
    }

}
