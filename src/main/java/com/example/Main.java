package com.example;

import com.example.util.ConsoleUI;
import com.example.service.FileService;
import com.example.service.TaskService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        FileService fileService = new FileService();

        // Запрос выбора интерфейса
        String[] options = {"Графический интерфейс", "Консольный интерфейс"};
        int choice = JOptionPane.showOptionDialog(null,
                "Выберите интерфейс:",
                "To-Do List",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            // Графический интерфейс
            SwingUtilities.invokeLater(() -> new TodoSwingApp(taskService, fileService));
        } else {
            // Консольный интерфейс
            ConsoleUI consoleUI = new ConsoleUI(taskService, fileService);
            consoleUI.start();
        }
    }
}


