package com.personalproject.doit.services;

import com.personalproject.doit.dtos.TaskAdminDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.TaskAdmin;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.TaskAdminRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminService {

    private TaskAdminRepository repository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private UserService userService;

    @Autowired
    public AdminService(TaskAdminRepository repository, UserRepository userRepository, TaskRepository taskRepository, UserService userService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public void addAdmin(Long id, Long userId) {
        if (!isUserAdmin(id)) {
            throw new ForbiddenException("You are not an admin of this task");
        }

        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        repository.addAdmin(id, userId);
    }

   @Transactional(readOnly = true)
   public boolean isUserAdmin(Long taskId) {
        UserMinDTO me = userService.getMe();

        Optional<Integer> result = repository.isUserAdmin(taskId, me.getId());

        return result.get() > 0;
    }

}
