package com.amarogamedev.plusgoals.services;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.dto.GoalDTO;
import com.amarogamedev.plusgoals.repository.GoalRepository;
import com.amarogamedev.plusgoals.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GoalService {

    @Autowired
    GoalRepository goalRepository;

    //cria um objeto de meta com base num corpo json
    public Goal fromDto(GoalDTO goalDTO) {
        return new Goal(goalDTO.getId(), goalDTO.getText(), goalDTO.getDone(), goalDTO.getChildrenIds(), goalDTO.getParentId());
    }

    //retorna todas as metas
    public List<Goal> findALl(){
        return goalRepository.findAll();
    }

    //retorna uma meta
    public Goal findById(String id) {
        Optional<Goal> goal = goalRepository.findById(id);
        return goal.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public Goal insert(Goal goal) {
        //o estado padrão para uma nova meta deve ser "a fazer"
        goal.setDone(false);
        goalRepository.insert(goal);
        //se foi informado uma meta superior, adicionar esta meta na hierarquia especificada
        if (goal.getParentId() != null) {
            addToParent(goal.getId(), findById(goal.getParentId()));
        }
        return goal;
    }

    public void delete(String goalId) {
        Goal goal = findById(goalId);
        //se a meta possui uma meta superior, remover ela da hierarquia
        if(goal.getParentId() != null) {
            removeFromParent(goalId,findById(goal.getParentId()));
        }
        //se a meta possui submetas, deletar elas também
        if(!goal.getChildrenIds().isEmpty()) {
            deleteAllChildren(goal);
        }
        goalRepository.delete(findById(goalId));
    }

    public Goal update(String goalId, Goal updatedGoal) {
        Goal goal = findById(goalId);
        //definir o texto e estado para o informado na requisição
        goal.setText(updatedGoal.getText());
        goal.setDone(updatedGoal.getDone());

        //se estiver definindo esta meta como feita, definir todas as submetas também
        if(!goal.getChildrenIds().isEmpty() && updatedGoal.getDone()) {
            markDoneAllChildren(goal);
        }

        return goalRepository.save(goal);
    }

    //remove a meta da hierarquia e insere abaixo da meta especificada
    public Goal changeParent(String goalId, String newParentGoal) {
        Goal goal = findById(goalId);
        removeFromParent(goalId, findById(goal.getParentId()));
        addToParent(goalId, findById(newParentGoal));
        return goal;
    }

    //adiciona na lista de submetas de uma meta
    private Goal addToParent(String goalId, Goal parentGoal) {
        parentGoal.addToChildrenList(goalId);
        return goalRepository.save(parentGoal);
    }

    //remove da lista de submetas de uma meta
    private Goal removeFromParent(String goalId, Goal parentGoal) {
        parentGoal.removeFromChildrenList(goalId);
        return goalRepository.save(parentGoal);
    }

    //apaga todas as submetas de uma meta
    private void deleteAllChildren (Goal goal) {
        for (int i = 0; i < goal.getChildrenIds().size(); i++) {
            findById(goal.getChildrenIds().get(i)).setParentId(null);
            delete(goal.getChildrenIds().get(i));
        }
    }

    private void markDoneAllChildren(Goal goal) {
        for (int i = 0; i < goal.getChildrenIds().size(); i++) {
            //define o estado de todas as submetas como feito
            Goal childGoal =findById(goal.getChildrenIds().get(i));
            childGoal.setDone(true);
            //se a submeta por sua vez possuir outras submetas, repetir o processo
            if(!childGoal.getChildrenIds().isEmpty()) {
                markDoneAllChildren(childGoal);
            }
        }
    }
}
