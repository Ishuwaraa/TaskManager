package com.treinetic.TaskManager.repository;

import com.treinetic.TaskManager.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<MyUser, UUID> {
    Optional<MyUser> findByEmail(String email);
}
