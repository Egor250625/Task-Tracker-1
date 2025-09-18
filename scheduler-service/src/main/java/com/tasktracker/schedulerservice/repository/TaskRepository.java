package com.tasktracker.schedulerservice.repository;

import com.tasktracker.schedulerservice.entity.Task;
import com.tasktracker.schedulerservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByOwnerAndStatus(User owner, String status);

    @Query("SELECT t FROM Task t WHERE t.owner = :owner AND t.status = :status AND t.doneAt >= :since")
    List<Task> findAllByOwnerAndStatusSince(@Param("owner") User owner,
                                            @Param("status") String status,
                                            @Param("since") LocalDateTime since);

    @Query("SELECT t FROM Task t WHERE t.owner = :owner AND t.status = 'COMPLETED' AND t.doneAt >= :since")
    List<Task> findCompletedSince(@Param("owner") User owner, @Param("since") LocalDateTime since);

    @Query("SELECT t FROM Task t WHERE t.owner = :owner AND t.status = 'IN_PROGRESS'")
    List<Task> findInProgress(@Param("owner") User owner);

}
