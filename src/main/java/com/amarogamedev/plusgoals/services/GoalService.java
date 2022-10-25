package com.amarogamedev.plusgoals.services;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.domain.Task;
import com.amarogamedev.plusgoals.dto.GoalDTO;
import com.amarogamedev.plusgoals.repository.GoalRepository;
import com.amarogamedev.plusgoals.repository.TaskRepository;
import com.amarogamedev.plusgoals.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    //cria um objeto de meta com base num corpo json
    public Goal fromDto(GoalDTO goalDTO) {
        return new Goal(goalDTO.getId(), goalDTO.getText(), goalDTO.getDone(), new ArrayList<>());
    }

    //retorna todas as metas
    public List<Goal> findAll(){
        return goalRepository.findAll();
    }

    //retorna uma meta
    public Goal findById(String id) {
        Optional<Goal> goal = goalRepository.findById(id);
        return goal.orElseThrow(() -> new ObjectNotFoundException("Meta não encontrada"));
    }

    //retorna todas as tarefas de uma meta
    public List<Task> findAllTasks(String id) {
        Goal goal = findById(id);
        List<Task> list = new ArrayList<>();
        for (int i = 0; i < goal.getTaskIds().size(); i++) {
            Task task = taskService.findById(goal.getTaskIds().get(i));
            list.add(task);
        }
        return list;
    }

    public Goal insert(Goal goal) {
        //o estado padrão para uma nova meta deve ser "a fazer"
        goal.setDone(false);
        goalRepository.insert(goal);
        return goal;
    }

    public void delete(String goalId) {
        Goal goal = findById(goalId);
        //se a meta possui tarefas, deletar elas também
        if(!goal.getTaskIds().isEmpty()) {
            deleteAllTasks(goal);
        }
        goalRepository.delete(findById(goalId));
    }

    public Goal update(String goalId, Goal updatedGoal) {
        Goal goal = findById(goalId);
        //definir o texto e estado para o informado na requisição
        goal.setText(updatedGoal.getText());
        goal.setDone(updatedGoal.getDone());

        //se estiver definindo esta meta como feita, definir todas as tarefas também
        if(!goal.getTaskIds().isEmpty() && updatedGoal.getDone()) {
            markDoneAllChildren(goal);
        }

        return goalRepository.save(goal);
    }

    public void removeTask(String goalId, String taskId) {
        Goal goal = findById(goalId);
        goal.getTaskIds().remove(taskId);
        goalRepository.save(goal);
    }

    public void addTask(String goalId, String taskId) {
        Goal goal = findById(goalId);
        goal.getTaskIds().add(taskId);
        goalRepository.save(goal);
    }

    //apaga todas as tarefas de uma meta
    private void deleteAllTasks(Goal goal) {
        for (int i = 0; i < goal.getTaskIds().size(); i++) {
            taskService.findById(goal.getTaskIds().get(i)).setGoalId(null);
            taskService.delete(goal.getTaskIds().get(i));
        }
    }

    //define o estado de todas as tarefas como feito
    private void markDoneAllChildren(Goal goal) {
        for (int i = 0; i < goal.getTaskIds().size(); i++) {
            taskService.findById(goal.getTaskIds().get(i)).setDone(true);
        }
    }
}
