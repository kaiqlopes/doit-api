package com.personalproject.doit.controllers;

import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.services.AdminService;
import com.personalproject.doit.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping(value = "/{id}/admins/{userId}")
    public ResponseEntity<Void> addAdmin(@PathVariable Long id, @PathVariable Long userId) {
        adminService.addAdmin(id, userId);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<TaskDTO> insert(@Valid @RequestBody TaskDTO dto) {
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
    @DeleteMapping(value = "/{id}/admins/{adminId}")
    public ResponseEntity<Void> removeAdminFromTask(@PathVariable Long id, @PathVariable Long adminId) {
        adminService.removeAdmin(id, adminId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}/users/{userId}")
    public ResponseEntity<Void> removeUserFromTask(@PathVariable Long id, @PathVariable Long userId) {
        service.removeUserFromTask(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @PostMapping(value = "/{id}/users")
    public ResponseEntity<Void> shareTask(@PathVariable Long id, @RequestParam(name = "email", defaultValue = "") String userEmail) {
        service.addTaskUser(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
