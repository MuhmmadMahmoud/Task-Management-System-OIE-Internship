package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public List<TaskResponse> getTasks(String username, Status status, Priority priority) {
        logger.info("Getting the tasks of user: " + username + " status=" + status + " priority=" + priority);
        User user = userService.findByUsername(username);

        List<Task> tasks;
        // the frontend can send both filters, one of them, or nothing
        if (status != null && priority != null) {
            tasks = taskRepository.findByUserAndStatusAndPriority(user, status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByUserAndStatus(user, status);
        } else if (priority != null) {
            tasks = taskRepository.findByUserAndPriority(user, priority);
        } else {
            tasks = taskRepository.findByUser(user);
        }

        List<TaskResponse> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(new TaskResponse(task));
        }
        return result;
    }

    @Override
    public TaskResponse getTaskById(Long id, String username) {
        Task task = findTaskOfUser(id, username);
        return new TaskResponse(task);
    }

    @Override
    public TaskResponse createTask(TaskRequest request, String username) {
        logger.info("Creating a new task for user: " + username);
        User user = userService.findByUsername(username);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUser(user);

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request, String username) {
        logger.info("Updating the task with id: " + id);
        Task task = findTaskOfUser(id, username);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        Task updatedTask = taskRepository.save(task);
        return new TaskResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id, String username) {
        logger.info("Deleting the task with id: " + id);
        Task task = findTaskOfUser(id, username);
        taskRepository.delete(task);
    }

    // get, update and delete all need this, so I wrote it one time
    private Task findTaskOfUser(Long id, String username) {
        User user = userService.findByUsername(username);
        return taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Task not found with id " + id));
    }
}
