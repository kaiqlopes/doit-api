package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.TaskAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskAdminRepository extends JpaRepository<TaskAdmin, Long>{

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tb_task_admins " +
            "WHERE user_id = :userId AND task_id = :taskId")
    Optional<Integer> isUserAdmin(Long taskId, Long userId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_task_admins " +
            "WHERE task_id = :taskId")
    void deleteAssociatedAdmins(Long taskId);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO tb_task_admins(task_id, user_id) " +
            "VALUES(:taskId, :userId)")
    void addAdmin(Long taskId, Long userId);

}
