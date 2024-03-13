package com.personalproject.doit.services;

import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.repositories.CategoryRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TaskServiceTests {

    @InjectMocks
    private TaskService service;

    @Mock
    private TaskRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminService adminService;

    @Mock
    private UserService userService;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Task task;
    private TaskDTO taskDTO;
    private User user;
    private UserMinDTO validUserMinDTO;
    private List<Task> taskList;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 10L;
        task = Factory.createTask();
        taskDTO = Factory.createTaskDTO();
        validUserMinDTO = Factory.createValidUserMinDTO();
        taskList = List.of(task);
        user = Factory.createUser();

        when(repository.findById(existingId)).thenReturn(Optional.of(task));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.validateTaskUser(existingId, 1L)).thenReturn(1);
        when(repository.validateTaskUser(existingId, 2L)).thenReturn(0);

        when(repository.findAllTasksByUserId(ArgumentMatchers.anyLong())).thenReturn(taskList);

        when(repository.save(ArgumentMatchers.any())).thenReturn(task);

        when(userService.authenticated()).thenReturn(user);

        doNothing().when(repository).addTaskUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        doNothing().when(repository).addAdmin(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
    }

    @Test
    public void findByIdShouldReturnTaskDTOWhenIdExists() {
        when(userService.getMe()).thenReturn(validUserMinDTO);

        TaskDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(TaskDTO.class, result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldNotThrowExceptionWhenValidTaskUser() {
        when(userService.getMe()).thenReturn(validUserMinDTO);

        Assertions.assertDoesNotThrow(() -> {
            TaskDTO result = service.findById(existingId);
        });
    }

    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenNotInvalidTaskUser() {
        UserMinDTO notValidUserMinDTO = Factory.createNotValidUserMinDTO();

        when(userService.getMe()).thenReturn(notValidUserMinDTO);

        Assertions.assertThrowsExactly(ForbiddenException.class, () -> {
            service.findById(existingId);
        });
    }

    @Test
    public void findAllShouldReturnList() {
        List<TaskDTO> result = service.findAll();

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnTaskDTO() {
        TaskDTO result = service.insert(taskDTO);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(TaskDTO.class, result);
    }

}
