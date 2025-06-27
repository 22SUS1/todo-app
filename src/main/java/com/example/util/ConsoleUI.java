package com.example.util;

import com.example.model.Task;
import com.example.model.TaskDto;
import com.example.service.TaskService;
import com.example.service.FileService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final TaskService taskService;
    private final FileService fileService;
    private final Scanner scanner;

    public ConsoleUI(TaskService taskService, FileService fileService) {
        this.taskService = taskService;
        this.fileService = fileService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // Загрузка задач при старте
        taskService.loadTasks(fileService.loadTasksFromFile());

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    viewAllTasks();
                    break;
                case "3":
                    toggleTaskStatus();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "5":
                    searchTasks();
                    break;
                case "6":
                    filterTasksByStatus();
                    break;
                case "7":
                    editTask();
                    break;
                case "0":
                    // Сохранение задач перед выходом
                    fileService.saveTasksToFile(taskService.getTasks());
                    System.out.println("Задачи сохранены. Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный ввод. Попробуйте еще раз.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Меню управления задачами ===");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Просмотреть все задачи");
        System.out.println("3. Изменить статус задачи");
        System.out.println("4. Удалить задачу");
        System.out.println("5. Поиск задач");
        System.out.println("6. Фильтрация по статусу");
        System.out.println("7. Редактировать задачу");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void addTask() {
        System.out.println("\n=== Добавление новой задачи ===");
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();

        System.out.print("Введите описание задачи (не обязательно): ");
        String description = scanner.nextLine();

        Task task = taskService.addTask(new TaskDto(name, description));
        System.out.println("Задача добавлена с ID: " + task.getId());
    }

    private void viewAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("\n=== Список всех задач ===");
            tasks.forEach(System.out::println);
        }
    }

    private void toggleTaskStatus() {
        viewAllTasks();
        if (taskService.getTasks().isEmpty()) return;

        System.out.print("Введите ID задачи для изменения статуса: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (taskService.toggleTaskStatus(id)) {
                System.out.println("Статус задачи изменен.");
            } else {
                System.out.println("Задача с таким ID не найдена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
        }
    }

    private void deleteTask() {
        viewAllTasks();
        if (taskService.getTasks().isEmpty()) return;

        System.out.print("Введите ID задачи для удаления: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (taskService.deleteTask(id)) {
                System.out.println("Задача удалена.");
            } else {
                System.out.println("Задача с таким ID не найдена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
        }
    }

    private void searchTasks() {
        System.out.print("Введите ключевое слово для поиска: ");
        String keyword = scanner.nextLine();
        List<Task> foundTasks = taskService.searchTasks(keyword);

        if (foundTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            System.out.println("\n=== Результаты поиска ===");
            foundTasks.forEach(System.out::println);
        }
    }

    private void filterTasksByStatus() {
        System.out.println("Фильтрация по статусу:");
        System.out.println("1. Выполненные задачи");
        System.out.println("2. Невыполненные задачи");
        System.out.print("Выберите вариант: ");
        String choice = scanner.nextLine();

        List<Task> filteredTasks;
        if (choice.equals("1")) {
            filteredTasks = taskService.filterTasksByStatus(true);
            System.out.println("\n=== Выполненные задачи ===");
        } else if (choice.equals("2")) {
            filteredTasks = taskService.filterTasksByStatus(false);
            System.out.println("\n=== Невыполненные задачи ===");
        } else {
            System.out.println("Неверный ввод.");
            return;
        }

        if (filteredTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            filteredTasks.forEach(System.out::println);
        }
    }

    private void editTask() {
        viewAllTasks();
        if (taskService.getTasks().isEmpty()) return;

        System.out.print("Введите ID задачи для редактирования: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Введите новое название (оставьте пустым, чтобы не изменять): ");
            String newName = scanner.nextLine();

            System.out.print("Введите новое описание (оставьте пустым, чтобы не изменять): ");
            String newDescription = scanner.nextLine();

            if (taskService.editTask(id,
                    newName.isEmpty() ? null : newName,
                    newDescription.isEmpty() ? null : newDescription)) {
                System.out.println("Задача успешно изменена.");
            } else {
                System.out.println("Задача с таким ID не найдена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
        }
    }
}