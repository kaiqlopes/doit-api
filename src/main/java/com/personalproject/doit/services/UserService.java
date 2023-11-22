package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.entities.Task;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserMinDTO findById(Long id) {
        User result = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        return new UserMinDTO(result);
    }

    @Transactional(readOnly = true)
    public Page<UserMinDTO> findAll(Pageable pageable) {
        Page<User> result = userRepository.findAll(pageable);
        return result.map(UserMinDTO::new);
    }

    @Transactional
    public UserMinDTO insert(UserDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);
        user = userRepository.save(user);
        return new UserMinDTO(user);
    }



    @Transactional
    public UserMinDTO update(Long id, UserDTO dto) {
        User user = userRepository.getReferenceById(id);
        copyDtoToEntity(dto, user);
        user = userRepository.save(user);
        return new UserMinDTO(user);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User doesnt exists");
        }

        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation", e);
        }

    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPassword(dto.getPassword());
    }
}
