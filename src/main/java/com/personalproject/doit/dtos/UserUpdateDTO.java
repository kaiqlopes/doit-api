package com.personalproject.doit.dtos;

import com.personalproject.doit.services.validation.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserMinDTO{

    public UserUpdateDTO() {
        super();
    }

    public UserUpdateDTO(Long id, String name, String email, String number) {
        super(id, name, email, number);
    }
}
