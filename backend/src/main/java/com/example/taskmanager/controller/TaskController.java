package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// the username comes from the token and not from the url, so nobody can
// write the name of another user and read his tasks
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getTasks(@RequestParam(required = false) Status status,
                                       @RequestParam(required = false) Priority priority,
                                       Authentication authentication) {
        logger.info("REST request to get the tasks");
        return taskService.getTasks(authentication.getName(), status, priority);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id, Authentication authentication) {
        logger.info("REST request to get the task with id: " + id);
        return taskService.getTaskById(id, authentication.getName());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request,
                                                   Authentication authentication) {
        logger.info("REST request to create a new task");
        TaskResponse created = taskService.createTask(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                                   @Valid @RequestBody TaskRequest request,
                                   Authentication authentication) {
        logger.info("REST request to update the task with id: " + id);
        return taskService.updateTask(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        logger.info("REST request to delete the task with id: " + id);
        taskService.deleteTask(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
