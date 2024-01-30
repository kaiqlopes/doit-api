package com.personalproject.doit.dtos;

import com.personalproject.doit.entities.TaskAdmin;

public class TaskAdminDTO {

    private Long id;
    private UserAdminDTO admin;
    private TaskDTO task;

    public TaskAdminDTO(Long id, UserAdminDTO admin, TaskDTO task) {
        this.id = id;
        this.admin = admin;
        this.task = task;
    }

    public TaskAdminDTO(TaskAdmin entity) {
        id = entity.getId();
        admin = new UserAdminDTO(entity.getAdmin());
    }

    public Long getId() {
        return id;
    }

    public UserAdminDTO getAdmin() {
        return admin;
    }

    public TaskDTO getTask() {
        return task;
    }
}
