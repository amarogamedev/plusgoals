package com.amarogamedev.plusgoals.dto;

import com.amarogamedev.plusgoals.domain.User;
import lombok.Data;

import java.math.BigInteger;

@Data
public class UserDTO {

    private BigInteger id;
    private String name;
    private String email;
    private String password;

    public UserDTO (User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
    }
}
