package com.personalproject.doit.controllers;

import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.services.AdminService;
import com.personalproject.doit.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private TaskService service;
    private AdminService adminService;

    @Autowired
    public TaskController(TaskService service, AdminService adminService) {
        this.service = service;
        this.adminService = adminService;
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @PostMapping(value = "{id}/{userId}/addAdmin")
    public ResponseEntity<String> addAdmin(@PathVariable Long id, @PathVariable Long userId) {
        adminService.addAdmin(id, userId);
        return ResponseEntity.ok("Admin added");
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<TaskDTO> dto = service.findAll();
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        TaskDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }



    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TaskDTO> insert(@RequestBody TaskDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }


    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}/{userId}")
    public ResponseEntity<String> removeUserFromTask(@PathVariable Long id, @PathVariable Long userId) {
        service.removeUserFromTask(id, userId);
        return ResponseEntity.ok("User removed successfully");
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @PostMapping(value = "/{id}")
    public ResponseEntity<String> shareTask(@PathVariable Long id, @RequestParam(name = "email", defaultValue = "") String userEmail) {
        service.shareTask(id, userEmail);
        return ResponseEntity.ok("Task successfully shared");
    }
}
