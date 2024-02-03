package com.personalproject.doit.services;

import com.personalproject.doit.dtos.CategoryDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.exceptions.ForbiddenException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.CategoryRepository;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<TaskDTO> findAll(Pageable pageable) {
        Page<Task> result = taskRepository.findAll(pageable);
        return result.map(TaskDTO::new);
    }

    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        UserMinDTO me = userService.getMe();

        Task entity = new Task();
        copyDtoToEntity(dto, entity);
        entity = taskRepository.save(entity);

        taskRepository.addAdmin(entity.getId(), me.getId());

        return new TaskDTO(entity);
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        if (!adminService.isUserAdmin(id)) {
            throw new ForbiddenException("You are not an admin of this task");
        }

        Task entity = taskRepository.getReferenceById(id);
        copyDtoToEntity(dto, entity);
        entity = taskRepository.save(entity);
        return new TaskDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("The resource you want to delete was not found");
        }

        if (!adminService.isUserAdmin(id)) {
            throw new ForbiddenException("You are not an admin of this task");
        }

        taskRepository.removeAssociatedAdmins(id);
        taskRepository.removeAllUsersFromTask(id);

        try {
            taskRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Referential integrity violation", e);
        }
    }

    //N+1
    @Transactional
    public void removeUserFromTask(Long taskId, Long userId) {
        if (!adminService.isUserAdmin(taskId)) {
            throw new ForbiddenException("You are not an admin of this task");
        }

        if (!taskRepository.existsById(taskId) || !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User or task id doesn't exist");
        }

        if (taskRepository.isUserAdmin(taskId, userId).get() > 0) {
            taskRepository.removeAdmin(userId);
        }

        taskRepository.removeUserFromTask(taskId, userId);
    }

    //N+1
    @Transactional
    public void shareTask(Long taskId, String userEmail) {
        if (!adminService.isUserAdmin(taskId)) {
            throw new ForbiddenException("You are not an admin of this task");
        }

        Long userId = userRepository.getUserIdByEmail(userEmail);

        if (userId == null) {
            throw new ResourceNotFoundException("The user you want to share the task doesn't exist");
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
