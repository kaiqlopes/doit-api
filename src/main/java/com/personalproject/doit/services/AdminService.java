package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        isUserAdmin(id);

        taskRepository.addAdmin(id, userId);
    }

   @Transactional(readOnly = true)
   public void isUserAdmin(Long taskId) {
        UserMinDTO me = userService.getMe();

        Integer result = taskRepository.isUserAdmin(taskId, me.getId());

        if (result == 0) {
            throw new ForbiddenException("You are not an admin of this task");
        }
    }

    @Transactional
    public void removeAdmin(Long id, Long adminId) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task doesn't exist");
        }

        if (!userRepository.existsById(adminId)) {
            throw new ResourceNotFoundException("User doesn't exist");
        }

        isUserAdmin(id);

        taskRepository.removeAdmin(id, adminId);
    }
}
