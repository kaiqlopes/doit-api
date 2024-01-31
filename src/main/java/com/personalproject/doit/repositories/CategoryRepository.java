package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
