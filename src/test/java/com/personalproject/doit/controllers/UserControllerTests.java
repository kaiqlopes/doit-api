package com.personalproject.doit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.dtos.UserUpdateDTO;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.repositories.UserRepository;
import com.personalproject.doit.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @MockBean
    private UserRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private UserDTO userDTO;
    private UserMinDTO userMinDTO;
    private UserUpdateDTO userUpdateDTO;
    private PageImpl<UserMinDTO> page;

    @Autowired
    public UserControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 2L;
        userDTO = Factory.createUserDTOWithNullId();
        userMinDTO = Factory.createValidUserMinDTO();
        page = new PageImpl<>(List.of(userMinDTO));
        userUpdateDTO = Factory.createUserUpdateDTO();

        when(service.getMe()).thenReturn(userMinDTO);
        when(service.findAll(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(userMinDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(userMinDTO);

        when(service.update(eq(existingId), any())).thenReturn(userMinDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).deleteById(existingId);
        doThrow(ResourceNotFoundException.class).when(service).deleteById(nonExistingId);
        doThrow(DatabaseException.class).when(service).deleteById(dependentId);
    }

    @Test
    public void getMeShouldReturnUserMinDTO() throws Exception {
        mockMvc.perform(get("/users/me")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").exists());
    }

    @Test
    public void findByIdShouldReturnUserMinDTOWhenIdExists() throws Exception {
        mockMvc.perform(get("/users/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void findByIdShouldReturnReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnUserMinDTOAndCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/users/register")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    public void updateShouldReturnUserMinDTOAndOkWhenIdDoesExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userUpdateDTO);

        mockMvc.perform(put("/users/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/users/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        mockMvc.perform(delete("/users/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());
    }
}
