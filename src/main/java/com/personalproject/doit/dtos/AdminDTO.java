package com.personalproject.doit.dtos;

import com.personalproject.doit.entities.Admin;

public class AdminDTO {

    private Long id;
    private UserAdminDTO admin;
    private TaskDTO task;

    public AdminDTO(Long id, UserAdminDTO admin, TaskDTO task) {
        this.id = id;
        this.admin = admin;
        this.task = task;
    }

    public AdminDTO(Admin entity) {
        id = entity.getId();
        admin = new UserAdminDTO(entity.getTaskAdmin());
        task = new TaskDTO(entity.getTask());
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
