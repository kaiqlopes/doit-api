package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.User;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private TaskRepository taskRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private User user;
    private UserDTO userDTO;
    private UserMinDTO userMinDTO;
    private PageImpl<User> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;
        dependentId = 2L;
        user = Factory.createUser();
        userDTO = Factory.createUserDTO();
        userMinDTO = Factory.createValidUserMinDTO();
        page = new PageImpl<>(List.of(user));

        when(repository.findById(existingId)).thenReturn(Optional.of(user));
        when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(repository.save(ArgumentMatchers.any())).thenReturn(user);
    }

    @Test
    public void findByIdShouldReturnUserMinDTOWhenIdExists() {
        UserMinDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(UserMinDTO.class, result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findAllShouldReturnUserMinDTOPage() {
        PageRequest page = PageRequest.of(0, 10);
        Page<UserMinDTO> result = service.findAll(page);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(Page.class, result);
        Assertions.assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    public void insertShouldReturnUserMinDTO() {
        UserMinDTO result = service.insert(userDTO);

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(UserMinDTO.class, result);
        Assertions.assertEquals(1L, result.getId());
    }

}
