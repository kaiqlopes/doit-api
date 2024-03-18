package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AdminServiceTests {

    @InjectMocks
    private AdminService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    private Long existingId;
    private Long nonExistingId;
    private UserMinDTO userMinDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        userMinDTO = Factory.createValidUserMinDTO();

        when(taskRepository.existsById(existingId)).thenReturn(true);
        when(taskRepository.existsById(nonExistingId)).thenReturn(false);

        when(userRepository.existsById(existingId)).thenReturn(true);
        when(userRepository.existsById(nonExistingId)).thenReturn(false);

        when(userService.getMe()).thenReturn(userMinDTO);

        when(taskRepository.isUserAdmin(ArgumentMatchers.anyLong(),  ArgumentMatchers.anyLong())).thenReturn(1);
    }

    @Test
    public void addAdminShouldThrowResourceNotFoundExceptionWhenTaskIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.addAdmin(nonExistingId, existingId);
        });
    }

    @Test
    public void addAdminShouldThrowResourceNotFoundExceptionWhenUserIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.addAdmin(existingId, nonExistingId);
        });
    }

    @Test
    public void addAdminShouldDoNothingWhenValidIds() {

        Assertions.assertDoesNotThrow(() -> {
            service.addAdmin(existingId, existingId);
        });
    }

    @Test
    public void isUserAdminShouldThrowForbiddenExceptionWhenUserIsNotAnAdmin() {
        when(taskRepository.isUserAdmin(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(0);

        Assertions.assertThrowsExactly(ForbiddenException.class, () -> {
            service.isUserAdmin(existingId);
        });
    }

    @Test
    public void isUserAdminShouldDoNothingWhenUserIsAnAdmin() {
        Assertions.assertDoesNotThrow(() -> {
            service.isUserAdmin(existingId);
        });
    }

    @Test
    public void removeAdminShouldThrowResourceNotFoundExceptionWhenTaskIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.removeAdmin(nonExistingId, existingId);
        });
    }

    @Test
    public void removeAdminShouldThrowResourceNotFoundExceptionWhenUserIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.removeAdmin(existingId, nonExistingId);
        });
    }

    @Test
    public void removeAdminShouldDoNothingWhenValidIds() {
        Assertions.assertDoesNotThrow(() -> {
            service.removeAdmin(existingId, existingId);
        });
    }

}
