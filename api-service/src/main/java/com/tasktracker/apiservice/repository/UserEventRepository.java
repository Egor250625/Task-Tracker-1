package com.tasktracker.apiservice.repository;

import com.tasktracker.apiservice.model.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserEventRepository extends JpaRepository<UserEvent,Integer> {

    @Query(value = """
        WITH chunk AS (
            SELECT ue.id
            FROM user_events ue
            WHERE ue.reserved_to <= now()
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
        )
        UPDATE user_events ue
        SET reserved_to = now() + (:delaySeconds * interval '1 second'),
            pull_count = ue.pull_count + 1
        FROM chunk c
        WHERE ue.id = c.id
        RETURNING ue.id, ue.pull_count, ue.message
        """, nativeQuery = true)
    List<UserEvent> fetchAndLockEvents(@Param("limit") int limit,@Param("delaySeconds") int delaySeconds);
}
