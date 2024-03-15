package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.Task;
import com.personalproject.doit.factories.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class TaskRepositoryTests {

    private long existingTaskId;
    private long existingUserId;
    private long nonExistingUserId;
    private int totalTaskCount;

    private TaskRepository repository;

    @Autowired
    public TaskRepositoryTests(TaskRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void SetUp() throws Exception {

        existingTaskId = 2L;
        existingUserId = 1L;
        nonExistingUserId = 100L;
        totalTaskCount = 13;
    }

    @Test
    public void isUserAdminShouldReturnNumberOneWhenUserIsAnAdmin() {

        Integer result = repository.isUserAdmin(1L, existingUserId);

        Assertions.assertEquals(1, result);
    }

    @Test
    public void isUserAdminShouldReturnNumberZeroWhenUserIsNotAnAdmin() {
        Integer result = repository.isUserAdmin(2L, existingUserId);

        Assertions.assertEquals(0, result);
    }

    @Test
    public void addAdminShouldAddUserAsAdmin() {
        repository.addAdmin(existingUserId, existingTaskId);

        Integer result = repository.isUserAdmin(existingTaskId, existingUserId);

        Assertions.assertEquals(1, result);
    }

    @Test
    public void addAdminShouldThrowExceptionWhenUserIsAlreadyTaskAdmin() {
        Assertions.assertThrows(Exception.class, () -> {
            repository.addAdmin(1L, existingUserId);
        });
    }

    @Test
    public void findAllTasksByUserIdShouldReturnListOfUserTasks() {
        int expectedTaskCount = 9;

        List<Task> result = repository.findAllTasksByUserId(existingUserId);

        Assertions.assertEquals(expectedTaskCount, result.size());
    }

    @Test
    public void findAllTasksByUserIdShouldReturnEmptyListWhenUserDoesNotExist() {
        List<Task> result = repository.findAllTasksByUserId(nonExistingUserId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void validateTaskUserShouldReturnNumberOneWhenUserIsTaskUser() {
        Integer result = repository.validateTaskUser(1L, existingUserId);

        Assertions.assertEquals(1, result);
    }

    @Test
    public void validateTaskUserShouldReturnNumberZeroWhenUserIsNotTaskUser() {
        Integer result = repository.validateTaskUser(12L, existingUserId);

        Assertions.assertEquals(0, result);
    }

    @Test
    public void validateTaskUserShouldReturnNumberZeroWhenTaskDoesNotExist() {
        Integer result = repository.validateTaskUser(14L, existingUserId);

        Assertions.assertEquals(0, result);
    }

    @Test
    public void findAssociatedTasksWithOnlyOneUserIdShouldReturnListWithTaskIds() {
        int expectedIdsCount = 5;

        List<Long> result = repository.findAssociatedTasksWithOnlyOneUserId(existingUserId);

        Assertions.assertEquals(expectedIdsCount, result.size());
    }

    @Test
    public void removeUserFromTaskShouldRemoveUser() {
        repository.removeUserFromTask(1L, existingUserId);

        Integer result = repository.validateTaskUser(1L, existingUserId);

        Assertions.assertEquals(0 , result);
    }

    @Test
    public void removeAllUsersFromTaskShouldRemoveAllUsers() {
        repository.removeAllUsersFromTask(existingTaskId);

        Integer result = repository.validateTaskUser(existingTaskId, existingUserId);

        Assertions.assertEquals(0, result);
    }

    @Test
    public void removeAdminShouldRemoveAdmin() {
        repository.removeAdmin(1L, existingUserId);

        Integer result = repository.isUserAdmin(1L, existingUserId);

        Assertions.assertEquals(0, result);

    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Task task = Factory.createTask();

        task = repository.save(task);
        List<Task> result = repository.findAll();

        Assertions.assertEquals(totalTaskCount + 1, result.size());
        Assertions.assertEquals(14L, task.getId());
    }

}
