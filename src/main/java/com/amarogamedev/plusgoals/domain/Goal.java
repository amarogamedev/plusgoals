package com.amarogamedev.plusgoals.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//modelo de entidade das metas
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="goals")
public class Goal implements Serializable {

    @Id
    private String id;
    private String text;
    private Boolean done;
    private List<String> taskIds = new ArrayList<>();
}