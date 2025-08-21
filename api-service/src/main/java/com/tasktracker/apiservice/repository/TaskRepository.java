package com.tasktracker.apiservice.repository;

import com.tasktracker.apiservice.model.entity.Task;
import com.tasktracker.apiservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findAllByOwner(User owner);
}
