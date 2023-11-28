package com.personalproject.doit.entities;

import com.personalproject.doit.enums.ToDoStatus;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.*;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "tb_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Integer priority;

    @Enumerated(EnumType.STRING)
    private ToDoStatus taskStatus;

    @ManyToMany(mappedBy = "tasks")
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "tb_task_categories",
        joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories = new HashSet<>();


    public Task(Long id, String title, String description, LocalDateTime startDate, LocalDateTime finishDate, Integer priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.priority = priority;
    }

    public Task(Long id, String title, String description, LocalDateTime startDate, LocalDateTime finishDate, Integer priority, ToDoStatus taskStatus, Set<Category> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.categories = categories;
    }

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setTaskStatus(ToDoStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public ToDoStatus getTaskStatus() {
        return taskStatus;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
