package com.personalproject.doit.services;

import com.personalproject.doit.config.AuthorizationServerConfig;
import com.personalproject.doit.dtos.CategoryDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.CategoryRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private AdminService adminService;
    private UserService userService;
    private AuthService authService;

    @Autowired
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository, UserRepository userRepository, AdminService adminService, UserService userService, AuthService authService) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.userService = userService;
        this.authService = authService;
    }


    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Task result = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found")
        );

        authService.validateTaskUser(id);

        return new TaskDTO(result);
    }


    @Transactional(readOnly = true)
    public List<TaskDTO> findAll() {
        User me = userService.authenticated();

        List<Task> result = taskRepository.findAllTasksByUserId(me.getId());

        return result.stream().map(TaskDTO::new).toList();
    }


    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        Task entity = new Task();
        copyDtoToEntity(dto, entity);

        entity = taskRepository.save(entity);

        User me = userService.authenticated();
        me.addTask(entity);

        taskRepository.addAdmin(entity.getId(), me.getId());

        return new TaskDTO(entity);
    }


    @Transactional
    public TaskDTO update(Long taskId, TaskDTO dto) {
        Task entity = taskRepository.getReferenceById(taskId);

        adminService.isUserAdmin(taskId);

        copyDtoToEntity(dto, entity);
        entity = taskRepository.save(entity);
        return new TaskDTO(entity);
    }


    @Transactional
    public void deleteById(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("The resource you want to delete was not found");
        }

        adminService.isUserAdmin(taskId);
        taskRepository.removeAllUsersFromTask(taskId);

        try {
            taskRepository.deleteById(taskId);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Referential integrity violation", e);
        }
    }


    @Transactional
    public void removeUserFromTask(Long taskId, Long userId) {
        if (!taskRepository.existsById(taskId) || !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User or task doesn't exist");
        }

        adminService.isUserAdmin(taskId);

        if (taskRepository.isUserAdmin(taskId, userId) > 0) {
            taskRepository.removeAdmin(taskId, userId);
        }

        taskRepository.removeUserFromTask(taskId, userId);
    }

    @Transactional
    public void shareTask(Long taskId, String userEmail) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Resource not found");
        }

        adminService.isUserAdmin(taskId);

        Optional<Long> result = userRepository.getUserIdByEmail(userEmail);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("The user you want to share the task doesn't exist");
        }

        Long userId = result.get();

        if (taskRepository.validateTaskUser(taskId, userId) > 0) {
            throw new DatabaseException("User already exists in the task");
        }

        taskRepository.shareTask(userId, taskId);
    }

    private void copyDtoToEntity(TaskDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setFinishDate(dto.getFinishDate());
        entity.setTaskStatus(dto.getTaskStatus());
        entity.setPriority(dto.getPriority());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category cat = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(cat);
        }
    }

}
