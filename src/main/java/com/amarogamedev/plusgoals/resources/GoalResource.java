package com.amarogamedev.plusgoals.resources;

import com.amarogamedev.plusgoals.domain.Goal;
import com.amarogamedev.plusgoals.dto.GoalDTO;
import com.amarogamedev.plusgoals.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/goals")
public class GoalResource {

    @Autowired
    private GoalService goalService;

    //retorna todas as metas primárias (nao possuem nenhuma meta acima)
    @GetMapping
    public ResponseEntity<List<GoalDTO>> findAll() {
        List<Goal> list = goalService.findALl();
        List<GoalDTO> listDto = list.stream()
                .filter(goal -> goal.getParentId() == null)
                .map(x -> new GoalDTO(x))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //retorna uma meta por id, independente da hierarquia
    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> findById(@PathVariable String id) {
        Goal goal = goalService.findById(id);
        return ResponseEntity.ok().body(new GoalDTO(goal));
    }

    //cria uma nova meta baseada no corpo que foi passado na requisição
    @PostMapping
    public ResponseEntity<Goal> insert(@RequestBody GoalDTO goalDto){
        Goal goal = goalService.fromDto(goalDto);
        goal = goalService.insert(goal);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(goal.getId()).toUri();
        return ResponseEntity.created(uri).body(goal);
    }

    //deleta uma meta por id, também deleta todas as metas abaixo dela
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id){
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //atualiza o texto ou estado de uma meta, marcar o estado como feito define todas as metas abaixo dela como feito também
    @PutMapping(value = "/{id}")
    public ResponseEntity<Goal> update(@RequestBody GoalDTO goalDto,@PathVariable String id){
        Goal goal = goalService.fromDto(goalDto);
        goal.setId(id);
        goal = goalService.update(id,goal);
        return ResponseEntity.ok().body(goal);
    }

    //remove a meta da hierarquia e insere abaixo da meta especificada
    @PutMapping(value = "/{id}/{newParentId}")
    public ResponseEntity<Goal> changeParent(@PathVariable String id, @PathVariable String newParentId){
        Goal goal = goalService.changeParent(id,newParentId);
        return ResponseEntity.ok().body(goal);
    }
}
