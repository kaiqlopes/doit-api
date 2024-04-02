package com.personalproject.doit.services;

import com.personalproject.doit.dtos.CategoryDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.CategoryRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    private TaskRepository repository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private AdminService adminService;
    private UserService userService;

    @Autowired
    public TaskService(TaskRepository repository, CategoryRepository categoryRepository, UserRepository userRepository, AdminService adminService, UserService userService) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.userService = userService;
    }


    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Task result = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found")
        );

        UserMinDTO me = userService.getMe();

        if (!me.hasRole("ROLE_ADMIN") && repository.validateTaskUser(id, me.getId()) == 0) {
            throw new ForbiddenException("Access denied");
        }

        return new TaskDTO(result);
    }


    @Transactional(readOnly = true)
    public List<TaskDTO> findAll() {
        UserMinDTO me = userService.getMe();

        List<Task> result = repository.findAllTasksByUserId(me.getId());

        return result.stream().map(TaskDTO::new).toList();
    }


    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        Task entity = new Task();
        copyDtoToEntity(dto, entity);
        UserMinDTO me = userService.getMe();

        entity = repository.save(entity);

        repository.addTaskUser(me.getId(), entity.getId());
        repository.addAdmin(me.getId(), entity.getId());

        return new TaskDTO(entity);
    }

    @Transactional
    public TaskDTO update(Long taskId, TaskDTO dto) {
        try {
            Task entity = repository.getReferenceById(taskId);
            copyDtoToEntity(dto, entity);
            adminService.isUserAdmin(taskId);
            entity = repository.save(entity);

            return new TaskDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }


    @Transactional
    public void deleteById(Long taskId) {
        if (!repository.existsById(taskId)) {
            throw new ResourceNotFoundException("The resource you want to delete was not found");
        }

        adminService.isUserAdmin(taskId);
        repository.removeAllUsersFromTask(taskId);

        try {
            repository.deleteById(taskId);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Referential integrity violation", e);
        }
    }


    @Transactional
    public void removeUserFromTask(Long taskId, Long userId) {
        if (!repository.existsById(taskId) || !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User or task doesn't exist");
        }

        adminService.isUserAdmin(taskId);

        repository.removeAdmin(taskId, userId);
        repository.removeUserFromTask(taskId, userId);
    }

    @Transactional
    public void addTaskUser(Long taskId, String userEmail) {
        if (!repository.existsById(taskId)) {
            throw new ResourceNotFoundException("Resource not found");
        }

        adminService.isUserAdmin(taskId);

        Optional<Long> result = userRepository.getUserIdByEmail(userEmail);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("The user you want to share the task doesn't exist");
        }

        Long userId = result.get();

        if (repository.validateTaskUser(taskId, userId) > 0) {
            throw new DatabaseException("User already exists in the task");
        }

        repository.addTaskUser(userId, taskId);
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
