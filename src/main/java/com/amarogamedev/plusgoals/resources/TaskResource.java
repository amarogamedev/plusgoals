package com.amarogamedev.plusgoals.resources;

import com.amarogamedev.plusgoals.domain.Task;
import com.amarogamedev.plusgoals.dto.TaskDTO;
import com.amarogamedev.plusgoals.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/tasks")
public class TaskResource {

    @Autowired
    private TaskService taskService;

    //retorna todas as tarefas
    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<Task> list = taskService.findALl();
        List<TaskDTO> listDto = list.stream()
                .map(x -> new TaskDTO(x))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //retorna uma tarefa por id
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable String id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok().body(new TaskDTO(task));
    }

    //cria uma nova tarefa baseada no corpo que foi passado na requisição
    @PostMapping
    public ResponseEntity<Task> insert(@RequestBody TaskDTO taskDto){
        Task task = taskService.fromDto(taskDto);
        task = taskService.insert(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).body(task);
    }

    //deleta uma tarefa por id
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //atualiza o texto ou estado de uma tarefa, marcar todas as tarefas como feitas marcará a meta como feita também
    @PutMapping(value = "/{id}")
    public ResponseEntity<Task> update(@RequestBody TaskDTO taskDto,@PathVariable String id){
        Task task = taskService.fromDto(taskDto);
        task.setId(id);
        task = taskService.update(id,task);
        return ResponseEntity.ok().body(task);
    }
}
