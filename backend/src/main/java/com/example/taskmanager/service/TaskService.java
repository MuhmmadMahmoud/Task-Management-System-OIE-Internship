package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;

import java.util.List;

// every method takes the username, so I work only on the tasks of that user
public interface TaskService {

    List<TaskResponse> getTasks(String username, Status status, Priority priority);

    TaskResponse getTaskById(Long id, String username);

    TaskResponse createTask(TaskRequest request, String username);

    TaskResponse updateTask(Long id, TaskRequest request, String username);

    void deleteTask(Long id, String username);
}
