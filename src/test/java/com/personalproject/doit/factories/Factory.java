package com.personalproject.doit.factories;

import com.personalproject.doit.dtos.TaskDTO;
import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.dtos.UserUpdateDTO;
import com.personalproject.doit.entities.Category;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;

import java.time.LocalDate;

public class Factory {

    public static Task createTask() {
        Task task = new Task(null, "Fazer o jantar", "Macarrão a bolonhesa com carne moída", null, null, 1);
        task.getCategories().add(createCategory());

        return task;
    }

    public static Task createTaskWithId() {
        Task task = new Task(1L, "Fazer o jantar", "Macarrão a bolonhesa com carne moída", null, null, 1);
        task.getCategories().add(createCategory());

        return task;
    }

    public static TaskDTO createTaskDTO() {
        return new TaskDTO(createTask());
    }
    public static TaskDTO createTaskDTOWithId() {
        return new TaskDTO(createTaskWithId());
    }

    public static TaskDTO createInvalidTaskDTO() {
        Task task = new Task(null, null, "Macarrão a bolonhesa com carne moída", null, null, 1);
        return new TaskDTO(task);
    }


    public static User createUser() {
        return new User(1L, "Kaique", "kaique@gmail.com", "40028922", LocalDate.parse("1998-11-20"), "1234567");
    }

    public static User createUserWithNullId() {
        return new User(null, "Kaique", "kaique@gmail.com", "40028922", LocalDate.parse("1998-11-20"), "1234567");
    }

    public static UserDTO createUserDTO() {
        return new UserDTO(createUser());
    }

    public static UserDTO createUserDTOWithNullId() {
        return new UserDTO(createUserWithNullId());
    }

    public static UserMinDTO createValidUserMinDTO() {
        UserMinDTO userMinDTO = new UserMinDTO(1L, "Kaique", "kaique@gmail.com", "1101234567");
        userMinDTO.getRoles().add("ROLE_ADMIN");
        userMinDTO.getRoles().add("ROLE_OPERATOR");

        return userMinDTO;
    }

    public static UserMinDTO createNotValidUserMinDTO() {
        UserMinDTO userMinDTO = new UserMinDTO(2L, "Not Valid User", "notvalid@gmail.com", "1101234588");
        userMinDTO.getRoles().add("ROLE_OPERATOR");

        return userMinDTO;
    }

    public static UserUpdateDTO createUserUpdateDTO() {
        return new UserUpdateDTO(null, "Kaique", "kaique@gmail.com", "123456789");
    }

    public static Category createCategory() {
        return new Category(1L, "Cuidados pessoais");
    }

    public static UserDetailsProjectionImpl createUserDetails() {
        UserDetailsProjectionImpl userDetailsProjection =  new UserDetailsProjectionImpl("kaique@gmail.com", "1234567", 1L, "ROLE_ADMIN");

        return userDetailsProjection;
    }
}
