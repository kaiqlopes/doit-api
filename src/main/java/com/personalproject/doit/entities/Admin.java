package com.personalproject.doit.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    private User taskAdmin;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Admin(User taskAdmin, Task task) {
        this.taskAdmin = taskAdmin;
        this.task = task;
    }

    public Admin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTaskAdmin() {
        return taskAdmin;
    }

    public void setTaskAdmin(User taskAdmin) {
        this.taskAdmin = taskAdmin;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
