package com.personalproject.doit.dtos;

import com.personalproject.doit.entities.Task;
import com.personalproject.doit.enums.ToDoStatus;


import java.time.LocalDateTime;

public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Integer priority;
    private ToDoStatus taskStatus;

    public TaskDTO(Long id, String title, String description, LocalDateTime startDate, LocalDateTime finishDate, Integer priority, ToDoStatus taskStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.priority = priority;
        this.taskStatus = taskStatus;
    }

    public TaskDTO(Task task) {
        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        startDate = task.getStartDate();
        finishDate = task.getFinishDate();
        priority = task.getPriority();
        taskStatus = task.getTaskStatus();
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

    public ToDoStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(ToDoStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
