package com.personalproject.doit.services;

import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.repositories.CategoryRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
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
    private UserMinDTO validUserMinDTO;
    private List<Task> taskList;
    private Category category;
    private String userEmail;
    private Long taskBelongingId;
    private Long notTaskBelongingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 10L;
        task = Factory.createTask();
        taskDTO = Factory.createTaskDTO();
        validUserMinDTO = Factory.createValidUserMinDTO();
        taskList = List.of(task);
        category = Factory.createCategory();
        userEmail = "kaique@gmail.com";
        taskBelongingId = 4L;
        notTaskBelongingId = 5L;

        when(repository.findById(existingId)).thenReturn(Optional.of(task));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(userService.getMe()).thenReturn(validUserMinDTO);

        when(repository.validateTaskUser(existingId, taskBelongingId)).thenReturn(1);
        when(repository.validateTaskUser(existingId, notTaskBelongingId)).thenReturn(0);

        when(repository.findAllTasksByUserId(ArgumentMatchers.anyLong())).thenReturn(taskList);

        when(repository.save(ArgumentMatchers.any())).thenReturn(task);

        doNothing().when(repository).addTaskUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        doNothing().when(repository).addAdmin(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        when(repository.getReferenceById(existingId)).thenReturn(task);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);

        when(userRepository.existsById(existingId)).thenReturn(true);
        when(userRepository.existsById(nonExistingId)).thenReturn(false);

        doNothing().when(repository).removeUserFromTask(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        doNothing().when(repository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        doNothing().when(adminService).isUserAdmin(ArgumentMatchers.anyLong());

        when(userRepository.getUserIdByEmail(userEmail)).thenReturn(Optional.of(taskBelongingId));
    }

    @Test
    public void findByIdShouldReturnTaskDTOWhenIdExists() {
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
        Assertions.assertDoesNotThrow(() -> {
            TaskDTO result = service.findById(existingId);
        });
    }

    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenInvalidTaskUser() {
        UserMinDTO notValidUserMinDTO = Factory.createNotValidUserMinDTO();

        when(userService.getMe()).thenReturn(notValidUserMinDTO);

        Assertions.assertThrowsExactly(ForbiddenException.class, () -> {
            service.findById(existingId);
        });
    }

    @Test
    public void findAllShouldReturnTaskDTOList() {
        List<TaskDTO> result = service.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(List.class, result);
    }

    @Test
    public void insertShouldReturnTaskDTO() {
        TaskDTO result = service.insert(taskDTO);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(TaskDTO.class, result);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        TaskDTO result = service.update(existingId, taskDTO);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(TaskDTO.class, result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, taskDTO);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.deleteById(nonExistingId);
        });

    }

    @Test
    public void deleteByIdShouldThrowDataIntegrityViolationExceptionWhenDependentId() {
        Assertions.assertThrowsExactly(DataIntegrityViolationException.class, () -> {
            service.deleteById(dependentId);
        });
    }

    @Test
    public void addTaskUserShouldThrowResourceNotFoundExceptionWhenTaskIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.addTaskUser(nonExistingId, userEmail);
        });
    }

    @Test
    public void addTaskUserShouldThrowResourceNotFoundExceptionWhenInvalidUserEmail() {
        String invalidUserEmail = "invalidEmail@gmail.com";

        when(userRepository.getUserIdByEmail(invalidUserEmail)).thenReturn(Optional.empty());

        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.addTaskUser(existingId, invalidUserEmail);
        });
    }

    @Test
    public void addTaskUserShouldThrowDatabaseExceptionWhenUserAlreadyInTheTask() {
        Assertions.assertThrowsExactly(DatabaseException.class, () -> {
            service.addTaskUser(existingId, userEmail);
        });
    }

    @Test
    public void addTaskUserShouldDoNothingWhenValidData() {
        when(userRepository.getUserIdByEmail(userEmail)).thenReturn(Optional.of(notTaskBelongingId));

        Assertions.assertDoesNotThrow(() -> {
            service.addTaskUser(existingId, userEmail);
        });
    }

    @Test
    public void removeUserFromTaskShouldThrowResourceNotFoundExceptionWhenTaskOrUserIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.removeUserFromTask(existingId, nonExistingId);
        });
    }

    @Test
    public void removeUserFromTaskShouldDoNothingWhenValidData() {
        Assertions.assertDoesNotThrow(() -> {
            service.removeUserFromTask(existingId, existingId);
        });
    }
}
