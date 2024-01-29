package com.personalproject.doit.controllers;

import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService service) {
        userService = service;
    }

    @GetMapping(value = "/me")
    public ResponseEntity<UserMinDTO> getMe() {
        UserMinDTO dto = userService.getMe();
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<UserMinDTO>> findAll(Pageable pageable) {
        Page<UserMinDTO> dto = userService.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @RequestMapping(value = "/{id}")
    public ResponseEntity<UserMinDTO> findById(@PathVariable Long id) {
        UserMinDTO dto = userService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserMinDTO> insert(@RequestBody UserDTO dto) {
        UserMinDTO minDto = userService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(minDto.getId()).toUri();

        return ResponseEntity.created(uri).body(minDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserMinDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        UserMinDTO minDto = userService.update(id, dto);
        return ResponseEntity.ok(minDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}