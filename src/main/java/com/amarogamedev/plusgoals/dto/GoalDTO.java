package com.amarogamedev.plusgoals.dto;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.repository.GoalRepository;
import com.amarogamedev.plusgoals.services.GoalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//define o formato do corpo utilizado nas requisições
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalDTO {

    private String id;
    private String text;
    private Boolean done;
    private List<String> childrenIds;
    private String parentId;


    public GoalDTO(Goal goal) {
        id = goal.getId();
        text = goal.getText();
        done = goal.getDone();
        childrenIds = goal.getChildrenIds();
        parentId = goal.getParentId();
    }
}
