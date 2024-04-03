package com.personalproject.doit.services.validation;

import com.personalproject.doit.dtos.FieldMessage;
import com.personalproject.doit.dtos.UserUpdateDTO;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private UserRepository repository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        Optional<User> user = repository.findByEmail(dto.getEmail());
        if (user.isPresent() && userId != user.get().getId()) {
            list.add(new FieldMessage("Email", "Email already exists"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getFieldMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}