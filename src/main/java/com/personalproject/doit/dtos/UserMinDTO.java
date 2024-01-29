package com.personalproject.doit.dtos;

import com.personalproject.doit.entities.Role;
import com.personalproject.doit.entities.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserMinDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;

    private Set<Role> roles = new HashSet<>();

    public UserMinDTO(Long id, String name, String email, String phone, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserMinDTO(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        phone = user.getPhone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
