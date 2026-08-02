package com.example.taskmanager.repository;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// every method takes the user, so a user can never reach the tasks of another one
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    List<Task> findByUserAndStatus(User user, Status status);

    List<Task> findByUserAndPriority(User user, Priority priority);

    List<Task> findByUserAndStatusAndPriority(User user, Status status, Priority priority);

    // id AND user together, so a task of somebody else comes back empty
    Optional<Task> findByIdAndUser(Long id, User user);
}
