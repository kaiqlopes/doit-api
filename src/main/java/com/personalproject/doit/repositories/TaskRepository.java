package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO tb_task_admins(task_id, user_id) " +
            "VALUES(:taskId, :userId)")
    void addAdmin(Long taskId, Long userId);

    @Query("SELECT DISTINCT obj FROM Task obj " +
            "JOIN FETCH obj.categories " +
            "JOIN obj.users u " +
            "WHERE u.id = :userId")
    List<Task> findAllTasksByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tb_task_admins " +
            "WHERE user_id = :userId AND task_id = :taskId")
    Integer isUserAdmin(Long taskId, Long userId);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tb_user_task " +
            "WHERE task_id = :taskId AND user_id = :userId")
    Integer validateTaskUser(Long taskId, Long userId);

    @Query(nativeQuery = true, value = "SELECT task_id " +
            "FROM tb_user_task " +
            "WHERE user_id = 1 " +
            "AND task_id NOT IN (SELECT task_id FROM tb_user_task WHERE user_id <> 1)")
    List<Long> findAssociatedTasksWithOnlyOneUserId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_user_task " +
            "WHERE user_id = :userId AND task_id = :id")
    void removeUserFromTask(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO tb_user_task " +
            "(user_id, task_id) VALUES (:userId, :id)")
    void shareTask(@Param("userId") Long UserId, @Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_user_task " +
            "WHERE task_id = :taskId")
    void removeAllUsersFromTask(Long taskId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_task_admins " +
            "WHERE task_id = :taskId AND user_id = :userId")
    void removeAdmin(Long taskId, Long userId);
}