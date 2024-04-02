package com.personalproject.doit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.token.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerIT {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TokenUtil tokenUtil;

    private Long existingTaskId;
    private Long nonExistingTaskId;
    private String adminUsername;
    private String adminPassword;
    private String operatorUsername;
    private String operatorPassword;
    private String bearerToken;

    @Autowired
    public TaskControllerIT(MockMvc mockMvc, ObjectMapper objectMapper, TokenUtil tokenUtil) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.tokenUtil = tokenUtil;
    }

    @BeforeEach
    void SetUp() throws Exception {
        existingTaskId = 1L;
        nonExistingTaskId = 100L;

        adminUsername = "kaique@gmail.com";
        adminPassword = "123456";
        bearerToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        operatorUsername = "gabriela@gmail.com";
        operatorPassword = "123456";
    }

    @Test
    public void findAllShouldReturnListOfTasksDTOAndOK() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void findAllShouldReturnUnauthorizedWhenUserIsNotLogged() throws Exception {
        bearerToken = null;

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnauthorized());
    }

    @Test
    public void addAdminShouldReturnIsNoContentWhenValidIds() throws Exception {
        mockMvc.perform(post("/tasks/{id}/admins/{userId}", existingTaskId, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void addAdminShouldReturnNotFoundWhenTaskIdDoesNotExist() throws Exception {
        mockMvc.perform(post("/tasks/{id}/admins/{userId}", nonExistingTaskId, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void addAdminShouldReturnNotFoundWhenUserIdDoesNotExist() throws Exception {
        mockMvc.perform(post("/tasks/{id}/admins/{userId}", nonExistingTaskId, 10L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void addAdminShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        mockMvc.perform(post("/tasks/{id}/admins/{userId}", 2L, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/{id}", nonExistingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenUserDoesNotBelongToTaskAndIsNotSystemAdmin() throws Exception {
        bearerToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, operatorPassword);

        mockMvc.perform(get("/tasks/{id}", 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnOkWhenValidData() throws Exception {
        mockMvc.perform(get("/tasks/{id}", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingTaskId))
                .andExpect(jsonPath("$.title").value("Estudar Java"));
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenInvalidData() throws Exception {
        TaskDTO taskDTO = Factory.createInvalidTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnCreatedWhenValidData() throws Exception {
        TaskDTO taskDTO = Factory.createTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

       mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(14L))
                .andExpect(jsonPath("$.title").value("Fazer o jantar"));

    }

    @Test
    public void updateShouldReturnNotFoundWhenTaskIdDoesNotExist() throws Exception {
        TaskDTO taskDTO = Factory.createTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", nonExistingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnForbiddenWhenUserIsNotTaskAdmin() throws Exception {
        TaskDTO taskDTO = Factory.createTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenInvalidData() throws Exception {
        TaskDTO taskDTO = Factory.createInvalidTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateShouldReturnObjectWhenValidData() throws Exception {
        TaskDTO taskDTO = Factory.createTaskDTO();
        String jsonBody = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/tasks/{id}", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Fazer o jantar"));
    }


    @Test
    public void deleteShouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", nonExistingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        bearerToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, operatorPassword);

        mockMvc.perform(delete("/tasks/{id}", 1L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }
    @Test
    public void deleteShouldReturnNoContentWhenValidData() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void removeAdminShouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", nonExistingTaskId, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void removeAdminShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", existingTaskId, 10L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void removeAdminShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        bearerToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, operatorPassword);

        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", existingTaskId, 1L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void removeAdminShouldReturnNoContentWhenValidData() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/admins/{adminId}", 3L, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void removeUserShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/users/{userId}", existingTaskId, 10L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void removeUserShouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/users/{userId}", nonExistingTaskId, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void removeUserShouldReturnForbiddenWhenUserIsNotAnAdmin() throws Exception {
        bearerToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, operatorPassword);

        mockMvc.perform(delete("/tasks/{id}/users/{userId}", existingTaskId, 1L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());
    }

    @Test
    public void removeUserShouldReturnNoContentWhenValidData() throws Exception {
        mockMvc.perform(delete("/tasks/{id}/users/{userId}", existingTaskId, 2L)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }

    @Test
    public void addTaskUserShouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", nonExistingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .param("email", "thorzinho@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void addTaskUserShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .param("email", "doesnotexist@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void addTaskUserShouldReturnBadRequestWhenAlreadyTaskUser() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .param("email", "gabriela@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTaskUserShouldReturnNoContentWhenValidData() throws Exception {
        mockMvc.perform(post("/tasks/{id}/users", existingTaskId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .param("email", "thorzinho@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());
    }
}