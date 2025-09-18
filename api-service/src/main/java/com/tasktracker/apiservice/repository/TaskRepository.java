package com.tasktracker.apiservice.repository;

import com.tasktracker.apiservice.model.entity.Task;
import com.tasktracker.apiservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    Page<Task> findAllByOwner(User owner, Pageable pageable);
}
