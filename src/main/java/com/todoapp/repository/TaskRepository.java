package com.todoapp.repository;

import com.todoapp.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private static final Logger logger = LogManager.getLogger(TaskRepository.class);
    private final Map<String, Task> tasks = new HashMap<>();

    public Task save(Task task) {
        logger.info("Saving task: {}", task.getTitle());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task findById(String id) {
        logger.info("Finding task by id: {}", id);
        return tasks.get(id);
    }

    public List<Task> findAll() {
        logger.info("Finding all tasks");
        return new ArrayList<>(tasks.values());
    }

    public void delete(String id) {
        logger.info("Deleting task with id: {}", id);
        tasks.remove(id);
    }

    public List<Task> findByCompleted(boolean completed) {
        logger.info("Finding tasks with completed status: {}", completed);
        return tasks.values().stream()
                .filter(task -> task.isCompleted() == completed)
                .toList();
    }
}