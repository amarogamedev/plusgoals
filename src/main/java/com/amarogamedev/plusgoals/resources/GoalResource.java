package com.amarogamedev.plusgoals.resources;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.domain.Task;
import com.amarogamedev.plusgoals.dto.GoalDTO;
import com.amarogamedev.plusgoals.dto.TaskDTO;
import com.amarogamedev.plusgoals.services.GoalService;
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
@RequestMapping(value = "/goals")
public class GoalResource {

    @Autowired
    private GoalService goalService;

    //retorna todas as metas
    @GetMapping
    public ResponseEntity<List<GoalDTO>> findAll() {
        List<Goal> list = goalService.findAll();
        List<GoalDTO> listDto = list.stream()
                .map(x -> new GoalDTO(x))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //retorna uma meta por id
    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> findById(@PathVariable String id) {
        Goal goal = goalService.findById(id);
        return ResponseEntity.ok().body(new GoalDTO(goal));
    }

    //retorna as tarefas pelo id da meta
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> findByGoalId(@PathVariable String id) {
        List<Task> list = goalService.findAllTasks(id);
        List<TaskDTO> listDto = list.stream()
                .map(x -> new TaskDTO(x))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //cria uma nova meta baseada no corpo que foi passado na requisição
    @PostMapping
    public ResponseEntity<Goal> insert(@RequestBody GoalDTO goalDto){
        Goal goal = goalService.fromDto(goalDto);
        goal = goalService.insert(goal);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(goal.getId()).toUri();
        return ResponseEntity.created(uri).body(goal);
    }

    //deleta uma meta por id, também deleta todas as tarefas dela
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id){
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //atualiza o texto ou estado de uma meta, marcar o estado como feito define todas as tarefas dela como feito também
    @PutMapping(value = "/{id}")
    public ResponseEntity<Goal> update(@RequestBody GoalDTO goalDto,@PathVariable String id){
        Goal goal = goalService.fromDto(goalDto);
        goal.setId(id);
        goal = goalService.update(id,goal);
        return ResponseEntity.ok().body(goal);
    }
}
