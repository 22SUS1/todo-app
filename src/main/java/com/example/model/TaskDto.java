package com.example.model;

public class TaskDto {
    private String name;
    private String description;

    // Конструктор с параметрами
    public TaskDto(String name, String description) {
        this.name = name;
        this.description = description;
    }


    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}