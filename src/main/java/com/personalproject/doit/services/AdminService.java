package com.personalproject.doit.services;

import com.personalproject.doit.dtos.AdminDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.entities.Admin;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.AdminRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private AdminRepository repository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Autowired
    public AdminService(AdminRepository repository, UserRepository userRepository, TaskRepository taskRepository) {
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
        Admin entity = new Admin(user, task);

        entity = repository.save(entity);

        return new AdminDTO(entity);
    }

}
