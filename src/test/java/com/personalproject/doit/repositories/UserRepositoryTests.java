package com.personalproject.doit.repositories;

import com.personalproject.doit.projections.UserDetailsProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    private Long existingId;
    private Long nonExistingId;
    private String existingUserEmail;
    private String nonExistingUserEmail;

    private UserRepository repository;

    @Autowired
    public UserRepositoryTests(UserRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void SetUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        existingUserEmail = "kaique@gmail.com";
        nonExistingUserEmail = "invalidemail@gmail.com";
    }

    @Test
    public void getUserIdByEmailShouldReturnOptionalWithUserIdWhenValidEmail() {


        Optional<Long> result = repository.getUserIdByEmail(existingUserEmail);

        Assertions.assertEquals(1L, result.get());
    }

    @Test
    public void getUserIdByEmailShouldReturnEmptyOptionalWhenInvalidEmail() {
        Optional<Long> result = repository.getUserIdByEmail(nonExistingUserEmail);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void searchUserAndRolesByEmailShouldReturnListWhenValidEmail() {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(existingUserEmail);

        Assertions.assertTrue(!result.isEmpty());
    }

    @Test
    public void searchUserAndRolesByEmailShouldReturnEmptyListWhenInvalidEmail() {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(nonExistingUserEmail);

        Assertions.assertTrue(result.isEmpty());
    }
}
