package com.amarogamedev.plusgoals.services;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.domain.Task;
import com.amarogamedev.plusgoals.dto.TaskDTO;
import com.amarogamedev.plusgoals.repository.GoalRepository;
import com.amarogamedev.plusgoals.repository.TaskRepository;
import com.amarogamedev.plusgoals.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    @Lazy
    GoalService goalService;

    //cria um objeto de tarefa com base num corpo json
    public Task fromDto(TaskDTO taskDTO) {
        return new Task(taskDTO.getId(), taskDTO.getText(), taskDTO.getDone(), taskDTO.getGoalId());
    }

    //retorna todas as tarefas
    public List<Task> findALl(){
        return taskRepository.findAll();
    }

    //retorna uma tarefa
    public Task findById(String id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException("Tarefa não encontrada"));
    }

    public Task insert(Task task) {
        //o estado padrão para uma nova tarefa deve ser "a fazer"
        task.setDone(false);
        taskRepository.insert(task);
        goalService.addTask(task.getGoalId(), task.getId());
        return task;
    }

    public void delete(String id) {
        Task task = findById(id);
        goalService.removeTask(task.getGoalId(), id);
        taskRepository.delete(task);
    }

    public Task update(String id, Task updatedTask) {
        Task task = findById(id);
        //definir o texto e estado para o informado na requisição
        task.setText(updatedTask.getText());
        task.setDone(updatedTask.getDone());
        return taskRepository.save(task);
    }
}
