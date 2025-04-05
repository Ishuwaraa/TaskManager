package com.treinetic.TaskManager.repository;

import com.treinetic.TaskManager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserEmail(String email);
    Optional<Task> findByIdAndUserEmail(Long id, String email);
}
