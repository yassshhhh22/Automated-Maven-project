package com.todoapp;

import com.todoapp.model.Task;
import com.todoapp.repository.TaskRepository;
import com.todoapp.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static TaskService taskService;
    private static Scanner scanner;

    public static void main(String[] args) {
        logger.info("Starting Todo Application");
        
        // Initialize dependencies
        TaskRepository taskRepository = new TaskRepository();
        taskService = new TaskService(taskRepository);
        scanner = new Scanner(System.in);
        
        boolean running = true;
        
        // Add some demo tasks
        taskService.createTask("Complete Java project", "Finish the Maven todo app project");
        taskService.createTask("Buy groceries", "Milk, eggs, bread");
        taskService.createTask("Call dentist", "Schedule appointment for next week");
        
        while (running) {
            printMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1 -> listAllTasks();
                case 2 -> listPendingTasks();
                case 3 -> listCompletedTasks();
                case 4 -> addNewTask();
                case 5 -> markTaskAsCompleted();
                case 6 -> deleteTask();
                case 0 -> {
                    running = false;
                    logger.info("Exiting application");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void printMenu() {
        System.out.println("\n===== TODO APP =====");
        System.out.println("1. List all tasks");
        System.out.println("2. List pending tasks");
        System.out.println("3. List completed tasks");
        System.out.println("4. Add new task");
        System.out.println("5. Mark task as completed");
        System.out.println("6. Delete task");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void listAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        displayTasks("ALL TASKS", tasks);
    }
    
    private static void listPendingTasks() {
        List<Task> pendingTasks = taskService.getPendingTasks();
        displayTasks("PENDING TASKS", pendingTasks);
    }
    
    private static void listCompletedTasks() {
        List<Task> completedTasks = taskService.getCompletedTasks();
        displayTasks("COMPLETED TASKS", completedTasks);
    }
    
    private static void displayTasks(String header, List<Task> tasks) {
        System.out.println("\n----- " + header + " -----");
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        
        for (Task task : tasks) {
            System.out.println("ID: " + task.getId());
            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Status: " + (task.isCompleted() ? "Completed" : "Pending"));
            System.out.println("Created: " + task.getCreatedAt());
            System.out.println("Updated: " + task.getUpdatedAt());
            System.out.println("-------------------");
        }
    }
    
    private static void addNewTask() {
        System.out.println("\n----- ADD NEW TASK -----");
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        
        Task task = taskService.createTask(title, description);
        System.out.println("Task added successfully with ID: " + task.getId());
    }
    
    private static void markTaskAsCompleted() {
        System.out.println("\n----- MARK TASK AS COMPLETED -----");
        System.out.print("Enter task ID: ");
        String id = scanner.nextLine();
        
        Task task = taskService.getTaskById(id);
        if (task == null) {
            System.out.println("Task not found with ID: " + id);
            return;
        }
        
        taskService.updateTask(id, null, null, true);
        System.out.println("Task marked as completed successfully.");
    }
    
    private static void deleteTask() {
        System.out.println("\n----- DELETE TASK -----");
        System.out.print("Enter task ID: ");
        String id = scanner.nextLine();
        
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task not found with ID: " + id);
        }
    }
}