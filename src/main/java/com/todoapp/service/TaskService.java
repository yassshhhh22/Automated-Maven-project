package com.todoapp.service;

import com.todoapp.model.Task;
import com.todoapp.repository.TaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title, String description) {
        logger.info("Creating new task: {}", title);
        Task task = new Task(title, description);
        return taskRepository.save(task);
    }

    public Task updateTask(String id, String title, String description, Boolean completed) {
        logger.info("Updating task with id: {}", id);
        Task task = taskRepository.findById(id);
        
        if (task == null) {
            logger.warn("Task not found with id: {}", id);
            return null;
        }
        
        if (title != null) {
            task.setTitle(title);
        }
        
        if (description != null) {
            task.setDescription(description);
        }
        
        if (completed != null) {
            task.setCompleted(completed);
        }
        
        return taskRepository.save(task);
    }

    public boolean deleteTask(String id) {
        logger.info("Deleting task with id: {}", id);
        Task task = taskRepository.findById(id);
        
        if (task == null) {
            logger.warn("Task not found with id: {}", id);
            return false;
        }
        
        taskRepository.delete(id);
        return true;
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompleted(true);
    }

    public List<Task> getPendingTasks() {
        return taskRepository.findByCompleted(false);
    }
}