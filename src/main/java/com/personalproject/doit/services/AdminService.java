package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminService {

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private UserService userService;

    @Autowired
    public AdminService(UserRepository userRepository, TaskRepository taskRepository, UserService userService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public void addAdmin(Long id, Long userId) {
        isUserAdmin(id);

        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        taskRepository.addAdmin(id, userId);
    }

   @Transactional(readOnly = true)
   public void isUserAdmin(Long taskId) {
        UserMinDTO me = userService.getMe();

        Optional<Integer> result = taskRepository.isUserAdmin(taskId, me.getId());

        if (result.get() == 0) {
            throw new ForbiddenException("You are not an admin of this task");
        }
    }

}
