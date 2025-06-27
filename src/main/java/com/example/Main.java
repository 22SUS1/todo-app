package com.example;

import com.example.service.FileService;
import com.example.service.TaskService;
import com.example.util.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        FileService fileService = new FileService();
        ConsoleUI consoleUI = new ConsoleUI(taskService, fileService);

        consoleUI.start();
    }
}
