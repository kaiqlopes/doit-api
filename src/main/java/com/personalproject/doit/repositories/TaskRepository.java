package com.personalproject.doit.repositories;

import com.personalproject.doit.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true, value = "SELECT task_id " +
            "FROM tb_user_task " +
            "WHERE user_id = 1 " +
            "AND task_id NOT IN (SELECT task_id FROM tb_user_task WHERE user_id <> 1)")
    List<Long> findAssociatedTasksWithOnlyOneUserId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_user_task " +
            "WHERE user_id = :userId AND task_id = :id")
    void removeUserFromTask(@Param("id") Long id, @Param("userId") Long userId);
}
