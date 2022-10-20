package com.amarogamedev.plusgoals.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

//modelo de entidade dos usuarios
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="users")
public class User implements Serializable {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
}