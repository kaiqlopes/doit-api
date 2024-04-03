package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.dtos.UserUpdateDTO;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.factories.Factory;
import com.personalproject.doit.projections.UserDetailsProjection;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private User user;
    private UserDTO userDTO;
    private UserMinDTO userMinDTO;
    private UserUpdateDTO userUpdateDTO;
    private PageImpl<User> page;
    private String existingEmail;
    private String nonExistingEmail;
    private UserDetailsProjection userDetailsProjection;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;
        dependentId = 2L;
        user = Factory.createUser();
        userDTO = Factory.createUserDTO();
        userMinDTO = Factory.createValidUserMinDTO();
        page = new PageImpl<>(List.of(user));
        existingEmail = "kaique@gmail.com";
        nonExistingEmail = "nonexisting@gmail.com";
        userDetailsProjection = Factory.createUserDetails();

        when(repository.findById(existingId)).thenReturn(Optional.of(user));
        when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(repository.save(ArgumentMatchers.any())).thenReturn(user);

        when(repository.getReferenceById(existingId)).thenReturn(user);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);

        doNothing().when(repository).deleteById(existingId);
        doNothing().when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        when(repository.searchUserAndRolesByEmail(existingEmail)).thenReturn(List.of(userDetailsProjection));
        when(repository.searchUserAndRolesByEmail(nonExistingEmail)).thenReturn(List.of());

        when(repository.findByEmail(existingEmail)).thenReturn(Optional.of(user));

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

    @Test
    public void updateShouldReturnUserMinDTOWhenIdExists() {
        userUpdateDTO = Factory.createUserUpdateDTO();

        UserMinDTO result = service.update(existingId, userUpdateDTO);

        Assertions.assertInstanceOf(UserMinDTO.class, result);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        userUpdateDTO = Factory.createUserUpdateDTO();

        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
           service.update(nonExistingId, userUpdateDTO);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> {
            service.deleteById(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
        Assertions.assertThrowsExactly(DatabaseException.class, () -> {
            service.deleteById(dependentId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.deleteById(existingId);
        });
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
        Assertions.assertThrowsExactly(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingEmail);
        });
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists() {
        UserDetails result = service.loadUserByUsername(existingEmail);

        Assertions.assertInstanceOf(UserDetails.class, result);
        Assertions.assertNotNull(result);
    }

}
