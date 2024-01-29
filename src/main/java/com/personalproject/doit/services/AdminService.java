package com.personalproject.doit.services;

import com.personalproject.doit.dtos.AdminDTO;
import com.personalproject.doit.entities.TaskAdmin;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.TaskAdminRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private TaskAdminRepository repository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Autowired
    public AdminService(TaskAdminRepository repository, UserRepository userRepository, TaskRepository taskRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public AdminDTO addAdmin(AdminDTO dto) {

        Long userId = dto.getAdmin().getId();
        Long taskId = dto.getTask().getId();

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found");
        }

        User user = userRepository.getReferenceById(userId);
        Task task = taskRepository.getReferenceById(taskId);
        TaskAdmin entity = new TaskAdmin(user, task);

        entity = repository.save(entity);

        return new AdminDTO(entity);
    }

    public boolean isUserAdmin(Long taskId, Long userId) {
        return repository.isUserAdmin(taskId, userId);
    }

}
