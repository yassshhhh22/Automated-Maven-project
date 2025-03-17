package com.todoapp.service;

import com.todoapp.model.Task;
import com.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;
    private Task testTask;

    @BeforeEach
    public void setup() {
        // Create mock repository
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
        
        // Setup test task
        testTask = new Task("Test Task", "Test Description");
        
        // Setup mock repository behavior
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(taskRepository.findById(testTask.getId())).thenReturn(testTask);
    }

    @Test
    public void testCreateTask() {
        // Act
        Task createdTask = taskService.createTask("New Task", "New Description");
        
        // Assert
        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        assertEquals("New Description", createdTask.getDescription());
        assertFalse(createdTask.isCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() {
        // Arrange
        String id = testTask.getId();
        
        // Act
        Task updatedTask = taskService.updateTask(id, "Updated Title", "Updated Description", true);
        
        // Assert
        assertNotNull(updatedTask);
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertTrue(updatedTask.isCompleted());
        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTaskNotFound() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(taskRepository.findById(nonExistentId)).thenReturn(null);
        
        // Act
        Task updatedTask = taskService.updateTask(nonExistentId, "Updated Title", "Updated Description", true);
        
        // Assert
        assertNull(updatedTask);
        verify(taskRepository, times(1)).findById(nonExistentId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        // Arrange
        String id = testTask.getId();
        
        // Act
        boolean result = taskService.deleteTask(id);
        
        // Assert
        assertTrue(result);
        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).delete(id);
    }

    @Test
    public void testDeleteTaskNotFound() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(taskRepository.findById(nonExistentId)).thenReturn(null);
        
        // Act
        boolean result = taskService.deleteTask(nonExistentId);
        
        // Assert
        assertFalse(result);
        verify(taskRepository, times(1)).findById(nonExistentId);
        verify(taskRepository, never()).delete(anyString());
    }

    @Test
    public void testGetAllTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(
            new Task("Task 1", "Description 1"),
            new Task("Task 2", "Description 2")
        );
        when(taskRepository.findAll()).thenReturn(expectedTasks);
        
        // Act
        List<Task> actualTasks = taskService.getAllTasks();
        
        // Assert
        assertEquals(expectedTasks.size(), actualTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetCompletedTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(
            new Task("Completed Task 1", "Description 1"),
            new Task("Completed Task 2", "Description 2")
        );
        when(taskRepository.findByCompleted(true)).thenReturn(expectedTasks);
        
        // Act
        List<Task> actualTasks = taskService.getCompletedTasks();
        
        // Assert
        assertEquals(expectedTasks.size(), actualTasks.size());
        verify(taskRepository, times(1)).findByCompleted(true);
    }

    @Test
    public void testGetPendingTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(
            new Task("Pending Task 1", "Description 1"),
            new Task("Pending Task 2", "Description 2")
        );
        when(taskRepository.findByCompleted(false)).thenReturn(expectedTasks);
        
        // Act
        List<Task> actualTasks = taskService.getPendingTasks();
        
        // Assert
        assertEquals(expectedTasks.size(), actualTasks.size());
        verify(taskRepository, times(1)).findByCompleted(false);
    }
}