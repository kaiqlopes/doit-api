package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private UserService userService;
    private TaskRepository taskRepository;

    public AuthService(UserService userService, TaskRepository taskRepository) {
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public void validateTaskUser(Long taskId) {
        UserMinDTO me = userService.getMe();

        if (taskRepository.validateTaskUser(taskId, me.getId()).get() < 1) {
            throw new ForbiddenException("Access denied");
        }
    }

}
