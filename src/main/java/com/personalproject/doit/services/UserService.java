package com.personalproject.doit.services;

import com.personalproject.doit.dtos.UserDTO;
import com.personalproject.doit.dtos.UserMinDTO;
import com.personalproject.doit.dtos.UserUpdateDTO;
import com.personalproject.doit.entities.Role;
import com.personalproject.doit.entities.User;
import com.personalproject.doit.exceptions.DatabaseException;
import com.personalproject.doit.exceptions.ResourceNotFoundException;
import com.personalproject.doit.projections.UserDetailsProjection;
import com.personalproject.doit.repositories.TaskRepository;
import com.personalproject.doit.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user = userRepository.save(user);

        return new UserMinDTO(user);
    }


    @Transactional
    public UserMinDTO update(Long id, UserUpdateDTO dto) {
        try {
            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, user);
            user = userRepository.save(user);
            return new UserMinDTO(user);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }

    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User doesn't exists");
        }

        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation", e);
        }

        List<Long> ids = taskRepository.findAssociatedTasksWithOnlyOneUserId(id);
        taskRepository.deleteAllById(ids);
    }

    private void copyDtoToEntity(UserMinDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);

        if (result.size() == 0) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());

        for (UserDetailsProjection projection : result) {
            user.addRoles(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");

            return userRepository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserMinDTO getMe() {
        User user = authenticated();
        return new UserMinDTO(user);
    }
}
