package com.example.service;

import com.example.model.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final String FILE_NAME = "tasks.txt";
    private static final String DELIMITER = "|";

    // Сохранение в текстовом формате
    public void saveTasksToFile(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(FILE_NAME)) {
            for (Task task : tasks) {
                writer.println(String.join(DELIMITER,
                        String.valueOf(task.getId()),
                        task.getName(),
                        task.getDescription() != null ? task.getDescription() : "",
                        String.valueOf(task.isDone()),
                        task.getCreatedAt().toString(),
                        task.getModifiedAt().toString()
                ));
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // Чтение из текстового файла
    public List<Task> loadTasksFromFile() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length == 6) {
                    Task task = new Task(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2].isEmpty() ? null : parts[2]
                    );
                    task.setDone(Boolean.parseBoolean(parts[3]));
                    task.setCreatedAt(LocalDateTime.parse(parts[4]));
                    task.setModifiedAt(LocalDateTime.parse(parts[5]));
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
        return tasks;
    }
}