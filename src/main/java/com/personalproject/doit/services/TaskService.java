package com.personalproject.doit.services;

import com.personalproject.doit.dtos.CategoryDTO;
import com.personalproject.doit.dtos.TaskCategoryDTO;
import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.CategoryRepository;
import com.personalproject.doit.repositories.TaskRepository;
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

    @Autowired
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public TaskCategoryDTO findById(Long id) {
        Task result = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new TaskCategoryDTO(result);
    }

    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        Page<Task> result = taskRepository.findAll(pageable);
        return result.map(TaskDTO::new);
    }

    @Transactional
    public TaskCategoryDTO insert(TaskCategoryDTO dto) {
        Task entity = new Task();
        copyDtoToEntity(dto, entity);
        entity = taskRepository.save(entity);
        return new TaskCategoryDTO(entity);
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        Task entity = taskRepository.getReferenceById(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setFinishDate(dto.getFinishDate());
        entity.setTaskStatus(dto.getTaskStatus());
        entity.setPriority(dto.getPriority());
        entity = taskRepository.save(entity);
        return new TaskDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("The resource you want to delete was not found");
        }

        try {
            taskRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity violation", e);
        }
    }

    private void copyDtoToEntity(TaskCategoryDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setFinishDate(dto.getFinishDate());
        entity.setTaskStatus(dto.getTaskStatus());
        entity.setPriority(dto.getPriority());

        for (CategoryDTO catDto : dto.getCategories()) {
            Category cat = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(cat);
        }
    }


}
