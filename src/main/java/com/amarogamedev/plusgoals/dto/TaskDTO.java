package com.amarogamedev.plusgoals.dto;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//define o formato do corpo utilizado nas requisições
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private String id;
    private String text;
    private Boolean done;
    private String goalId;


    public TaskDTO(Task task) {
        id = task.getId();
        text = task.getText();
        done = task.getDone();
        goalId = task.getId();
    }
}
