package com.personalproject.doit.controllers;

import com.personalproject.doit.dtos.TaskCategoryDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService service) {
        taskService = service;
    }

    @GetMapping
    public ResponseEntity<Page<TaskDTO>> findAll(Pageable pageable) {
        Page<TaskDTO> dto = taskService.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}/categories")
    public ResponseEntity<TaskCategoryDTO> findByIdWithCategories(@PathVariable Long id) {
        TaskCategoryDTO dto = taskService.findByIdWithCategories(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        TaskDTO dto = taskService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TaskCategoryDTO> insert(@RequestBody TaskCategoryDTO dto) {
        dto = taskService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        dto = taskService.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}/{userId}")
    public ResponseEntity<String> removeUserFromTask(@PathVariable Long id, @PathVariable Long userId) {
        taskService.removeUserFromTask(id, userId);
        return ResponseEntity.ok("User removed successfully");
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<String> shareTask(@PathVariable Long id, @RequestParam(name = "email", defaultValue = "") String userEmail) {
        taskService.shareTask(id, userEmail);
        return ResponseEntity.ok("Task successfully shared");
    }
}
