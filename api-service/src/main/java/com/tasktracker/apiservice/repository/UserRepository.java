package com.tasktracker.apiservice.repository;

import com.tasktracker.apiservice.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByEmail(@NotBlank String email);

    boolean existsByEmail(@NotBlank String email);

    User findUserById(Integer id);
}
