package com.personalproject.doit.services;

import com.personalproject.doit.dtos.CategoryDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category result = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new CategoryDTO(result);
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> result = categoryRepository.findAll(pageable);
        return result.map(CategoryDTO::new);
    }
    
}
