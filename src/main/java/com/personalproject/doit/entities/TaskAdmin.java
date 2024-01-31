package com.personalproject.doit.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_task_admins")
public class TaskAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskAdmin taskAdmin = (TaskAdmin) o;

        if (!Objects.equals(admin, taskAdmin.admin)) return false;
        return Objects.equals(task, taskAdmin.task);
    }

    @Override
    public int hashCode() {
        int result = admin != null ? admin.hashCode() : 0;
        result = 31 * result + (task != null ? task.hashCode() : 0);
        return result;
    }
}
