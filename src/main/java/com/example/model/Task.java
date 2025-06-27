package com.example.model;


import java.time.LocalDateTime;

public class Task {

    private int id;
    private String name;
    private String description;
    private boolean isDone;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Конструктор, геттеры и сеттеры
    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDone = false;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    // Остальные геттеры и сеттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.modifiedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.modifiedAt = LocalDateTime.now();
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
        this.modifiedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    // Сеттеры для дат
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

        @Override
    public String toString() {
        return String.format(
                "ID: %d | Название: %s | Описание: %s | Статус: %s | Создана: %s | Изменена: %s",
                id, name, description, isDone ? "Выполнена" : "Не выполнена",
                createdAt, modifiedAt
        );
    }
}