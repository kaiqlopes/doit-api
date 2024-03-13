package com.personalproject.doit.factories;

import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;

import java.time.Instant;
import java.time.LocalDateTime;

public class TaskFactory {

    public static Task createTask() {
        Task task = new Task(null, "Fazer o jantar", "Macarrão a bolonhesa com carne moída", LocalDateTime.now(), null, 1);
        task.getCategories().add(new Category(1L, "Obrigações diárias"));

        return task;
    }

}
