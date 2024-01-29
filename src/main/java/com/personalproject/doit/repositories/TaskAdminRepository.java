package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.TaskAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAdminRepository extends JpaRepository<TaskAdmin, Long>{

    @Query(nativeQuery = true, value = "SELECT task_id, admin_id FROM tb_task_admins " +
            "WHERE admin_id = :userId AND task_id = :taskId")
    boolean isUserAdmin(Long taskId, Long userId);

}
