package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.TaskAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskAdminRepository extends JpaRepository<TaskAdmin, Long>{

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tb_task_admins " +
            "WHERE user_id = :userId AND task_id = :taskId")
    Optional<Integer> isUserAdmin(Long taskId, Long userId);

}
