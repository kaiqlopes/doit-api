package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "SELECT id FROM tb_user " +
            "WHERE email = :email")
    Long getUserIdByEmail(@Param("email") String email);
}
