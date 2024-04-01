package com.personalproject.doit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.services.AdminService;
import com.personalproject.doit.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = TaskController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
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
    private String existingEmail;
    private String nonExistingEmail;


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
        existingEmail = "existingemail@gmail.com";
        nonExistingEmail = "nonexistingemail@gmail.com";

        doNothing().when(adminService).addAdmin(existingId, existingId);
        doThrow(ResourceNotFoundException.class).when(adminService).addAdmin(existingId, nonExistingId);
        doThrow(ResourceNotFoundException.class).when(adminService).addAdmin(nonExistingId, existingId);
        doThrow(ResourceNotFoundException.class).when(adminService).addAdmin(nonExistingId, nonExistingId);

        when(service.findAll()).thenReturn(List.of(taskDTO));

        when(service.findById(existingId)).thenReturn(taskDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(taskDTO);

        when(service.update(eq(existingId), any())).thenReturn(taskDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        doNothing().when(adminService).isUserAdmin(any());

        doNothing().when(service).deleteById(existingId);
        doThrow(ResourceNotFoundException.class).when(service).deleteById(nonExistingId);
        doThrow(DatabaseException.class).when(service).deleteById(dependentId);

        doNothing().when(adminService).removeAdmin(existingId, existingId);
        doThrow(ResourceNotFoundException.class).when(adminService).removeAdmin(eq(nonExistingId), anyLong());
        doThrow(ResourceNotFoundException.class).when(adminService).removeAdmin(anyLong(), eq(nonExistingId));
        doNothing().when(adminService).isUserAdmin(anyLong());

        doNothing().when(service).removeUserFromTask(existingId, existingId);
        doThrow(ResourceNotFoundException.class).when(service).removeUserFromTask(eq(nonExistingId), anyLong());
        doThrow(ResourceNotFoundException.class).when(service).removeUserFromTask(anyLong(), eq(nonExistingId));
        doThrow(ResourceNotFoundException.class).when(service).removeUserFromTask(nonExistingId, nonExistingId);

        doNothing().when(service).addTaskUser(existingId, existingEmail);
        doThrow(ResourceNotFoundException.class).when(service).addTaskUser(eq(nonExistingId), any());
        doThrow(ResourceNotFoundException.class).when(service).addTaskUser(anyLong(), eq(nonExistingEmail));
    }

    @Test
    public void addAdminShouldReturnIsNoContentWhenValidIds() throws Exception {

        mockMvc.perform(post("/tasks/{id}/admins/{userId}", existingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void addAdminShouldReturnNotFoundWhenIdsDoesNotExist() throws Exception {

        mockMvc.perform(post("/tasks/{id}/admins/{userId}", nonExistingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnListAndOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/tasks")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void findByIdShouldReturnUserDTOAndOk() throws Exception {
        mockMvc.perform(get("/tasks/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnDTOAndCreatedWhenValidObject() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(post("/tasks")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenInvalidObject() throws Exception {
        TaskDTO invalidTaskDTO = Factory.createInvalidTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(invalidTaskDTO);

        mockMvc.perform(post("/tasks")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateShouldReturnDTOAndOkWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(jsonBody))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        when(service.update(anyLong(), any())).thenThrow(ForbiddenException.class);

        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeAdminFromTaskShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", existingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void removeAdminFromTaskShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        doThrow(ForbiddenException.class).when(adminService).removeAdmin(anyLong(), anyLong());

        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", existingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void removeAdminFromTaskShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", nonExistingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void removeUserFromTaskShouldReturnNoContentValidIds() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/users/{userId}", existingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void removeUserFromTaskShouldReturnIsNotFoundWhenInvalidIds() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/users/{userId}", nonExistingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void removeUserFromTaskShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        doThrow(ForbiddenException.class).when(service).removeUserFromTask(anyLong(), anyLong());

        mockMvc.perform(delete("/tasks/{id}/users/{userId}", existingId, existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void shareTaskShouldReturnNoContentWhenValidData() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", existingId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void shareTaskShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", nonExistingId)
                        .param("email", existingEmail)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void shareTaskShouldReturnNotFoundWhenEmailDoesNotExist() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", existingId)
                        .param("email", nonExistingEmail)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }
}
