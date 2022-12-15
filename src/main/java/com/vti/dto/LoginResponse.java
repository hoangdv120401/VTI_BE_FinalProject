package com.vti.dto;

import com.vti.entity.Enum.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

    public LoginResponse id(Integer id) {
        this.id = id;
        return this;
    }

    public LoginResponse username(String username) {
        this.username = username;
        return this;
    }

    public LoginResponse password(String password) {
        this.password = password;
        return this;
    }

    public LoginResponse firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public LoginResponse lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LoginResponse role(Role role) {
        this.role = role;
        return this;
    }
}
