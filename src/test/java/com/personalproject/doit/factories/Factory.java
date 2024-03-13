package com.personalproject.doit.factories;

import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Role;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Factory {

    public static Task createTask() {
        Task task = new Task(null, "Fazer o jantar", "Macarrão a bolonhesa com carne moída", LocalDateTime.now(), null, 1);
        task.getCategories().add(new Category(1L, "Obrigações diárias"));

        return task;
    }

    public static TaskDTO createTaskDTO() {
        return new TaskDTO(createTask());
    }

    public static UserMinDTO createValidUserMinDTO() {
        UserMinDTO userMinDTO = new UserMinDTO(1L, "Kaique", "kaique@gmail.com", "1101234567");
        userMinDTO.getRoles().add("ROLE_ADMIN");
        userMinDTO.getRoles().add("ROLE_OPERATOR");

        return userMinDTO;
    }

    public static UserMinDTO createNotValidUserMinDTO() {
        UserMinDTO userMinDTO = new UserMinDTO(2L, "Thorzinho", "thorzinho@gmail.com", "1101234588");
        userMinDTO.getRoles().add("ROLE_OPERATOR");

        return userMinDTO;
    }

    public static User createUser() {
        return new User(1L, "Kaique", "kaique@gmail.com", "40028922", LocalDate.parse("1998-11-20"), "123456");
    }
}
