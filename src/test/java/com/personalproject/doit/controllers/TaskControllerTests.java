package com.personalproject.doit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.services.AdminService;
import com.personalproject.doit.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService service;

    @MockBean
    private AdminService adminService;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    private TaskDTO taskDTO;

    @Autowired
    public TaskControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 2L;
        taskDTO = Factory.createTaskDTOWithId();

        doNothing().when(adminService).addAdmin(existingId, existingId);
        doThrow(ResourceNotFoundException.class).when(adminService).addAdmin(existingId, nonExistingId);
        doThrow(ResourceNotFoundException.class).when(adminService).addAdmin(nonExistingId, existingId);
        doThrow(ResourceNotFoundException.class).when(adminService).addAdmin(nonExistingId, nonExistingId);

        when(service.findAll()).thenReturn(List.of(taskDTO));

        when(service.findById(existingId)).thenReturn(taskDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(taskDTO);
    }

    @Test
    @WithMockUser(username = "kaique@gmail.com", password = "123456")
    public void addAdminShouldReturnIsNoContentWhenValidIds() throws Exception {
        String expectedString = "admin added";

        mockMvc.perform(post("/tasks/{id}/admins/{userId}", existingId, existingId)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .with(user("kaiq")))

                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "kaique@gmail.com", password = "123456")
    public void addAdminShouldReturnNotFoundWhenIdsDoesNotExist() throws Exception {
        String expectedString = "admin added";

        mockMvc.perform(post("/tasks/{id}/admins/{userId}", nonExistingId, existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user("kaiq")))

                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "kaique@gmail.com", password = "123456")
    public void findAllShouldReturnListAndOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("kaiq")))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username = "kaique@gmail.com", password = "123456")
    public void findByIdShouldReturnUserDTOAndOk() throws Exception {
        mockMvc.perform(get("/tasks/{id}", existingId)
                .with(csrf())
                .with(user("kaiq")))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "kaique@gmail.com", password = "123456")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/{id}", nonExistingId)
                        .with(csrf())
                        .with(user("kaiq")))

                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "kaique@gmail.com", password = "123456")
    public void insertShouldReturnDTOAndCreatedWhenValidObject() throws Exception {
        String jsonString = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(post("/tasks")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("kaiq"))
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

}
