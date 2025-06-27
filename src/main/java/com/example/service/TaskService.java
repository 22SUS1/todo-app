package com.example.service;


import com.example.model.Task;
import com.example.model.TaskDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    // Добавление задачи
    public Task addTask(TaskDto taskDto) {
        Task task = new Task(nextId++, taskDto.getName(), taskDto.getDescription());
        tasks.add(task);
        return task;
    }

    // Просмотр всех задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // Изменение статуса задачи
    public boolean toggleTaskStatus(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setDone(!task.isDone());
                return true;
            }
        }
        return false;
    }

    // Удаление задачи
    public boolean deleteTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    // Поиск по ключевому слову
    public List<Task> searchTasks(String keyword) {
        return tasks.stream()
                .filter(task -> task.getName().contains(keyword) ||
                        (task.getDescription() != null && task.getDescription().contains(keyword)))
                .collect(Collectors.toList());
    }

    // Фильтрация по статусу
    public List<Task> filterTasksByStatus(boolean isDone) {
        return tasks.stream()
                .filter(task -> task.isDone() == isDone)
                .collect(Collectors.toList());
    }

    // Редактирование задачи
    public boolean editTask(int id, String newName, String newDescription) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                if (newName != null) task.setName(newName);
                if (newDescription != null) task.setDescription(newDescription);
                return true;
            }
        }
        return false;
    }

    // Загрузка задач
    public void loadTasks(List<Task> loadedTasks) {
        this.tasks = new ArrayList<>(loadedTasks);
        this.nextId = loadedTasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}