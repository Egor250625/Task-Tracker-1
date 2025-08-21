package com.tasktracker.schedulerservice.repository;

import com.tasktracker.schedulerservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserById(Integer id);
}
