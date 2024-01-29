package com.personalproject.doit.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_task_admins")
public class TaskAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    private User admin;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskAdmin(User taskAdmin, Task task) {
        this.admin = taskAdmin;
        this.task = task;
    }

    public TaskAdmin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}