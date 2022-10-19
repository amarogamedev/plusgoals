package com.amarogamedev.plusgoals.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection="users")
public class User implements Serializable {

    @Id
    private BigInteger id;
    private String name;
    private String email;
    private String password;
}