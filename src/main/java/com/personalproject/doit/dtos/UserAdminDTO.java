package com.personalproject.doit.dtos;

import com.personalproject.doit.entities.User;

public class UserAdminDTO {

    private Long id;
    private String email;

    public UserAdminDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserAdminDTO(User entity) {
        id = entity.getId();
        email = entity.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
