package com.personalproject.doit.services;

import com.personalproject.doit.dtos.CategoryDTO;
import com.personalproject.doit.dtos.TaskCategoryDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository repository) {
        taskRepository = repository;
    }

    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Task result = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new TaskDTO(result);
    }

    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        Page<Task> result = taskRepository.findAll(pageable);
        return result.map(TaskDTO::new);
    }

    @Transactional
    public TaskCategoryDTO insert(TaskCategoryDTO dto) {
        Task task = new Task();
        copyDtoToEntity(dto, task);
        task = taskRepository.save(task);
        return new TaskCategoryDTO(task);
    }

    private void copyDtoToEntity(TaskCategoryDTO dto, Task task) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setFinishDate(dto.getFinishDate());
        task.setTaskStatus(dto.getTaskStatus());
        task.setPriority(dto.getPriority());

        for (CategoryDTO catDto : dto.getCategory()) {
            Category cat = new Category();
            cat.setId(catDto.getId());
            cat.setName(catDto.getName());
            task.getCategories().add(cat);
        }
    }
}
