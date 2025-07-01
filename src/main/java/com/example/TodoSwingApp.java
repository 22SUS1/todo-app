package com.example;

import com.example.model.TaskDto;
import com.example.model.Task;
import com.example.service.FileService;
import com.example.service.TaskService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TodoSwingApp {
    private final TaskService taskService;
    private final FileService fileService;
    private JFrame frame;
    private DefaultListModel<String> listModel;
    private JList<String> tasksList;

    public TodoSwingApp(TaskService taskService, FileService fileService) {
        this.taskService = taskService;
        this.fileService = fileService;

        // Загружаем задачи при старте
        taskService.loadTasks(fileService.loadTasksFromFile());

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Менеджер задач");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Создаем модель списка и сам список
        listModel = new DefaultListModel<>();
        tasksList = new JList<>(listModel);
        tasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTasksList();

        // Панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Кнопки управления
        JButton addButton = new JButton("Добавить");
        JButton toggleButton = new JButton("Изменить статус");
        JButton deleteButton = new JButton("Удалить");
        JButton editButton = new JButton("Редактировать");
        JButton searchButton = new JButton("Поиск");
        JButton filterButton = new JButton("Фильтр по статусу");
        JButton saveButton = new JButton("Сохранить");

        // Добавляем обработчики событий
        addButton.addActionListener(e -> showAddTaskDialog());
        toggleButton.addActionListener(e -> toggleTaskStatus());
        deleteButton.addActionListener(e -> deleteTask());
        editButton.addActionListener(e -> showEditTaskDialog());
        searchButton.addActionListener(e -> showSearchDialog());
        filterButton.addActionListener(e -> showFilterDialog());
        saveButton.addActionListener(e -> saveTasks());

        // Добавляем кнопки на панель
        buttonPanel.add(addButton);
        buttonPanel.add(toggleButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(saveButton);

        // Добавляем компоненты на форму
        frame.add(new JScrollPane(tasksList), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateTasksList() {
        listModel.clear();
        for (Task task : taskService.getAllTasks()) {
            listModel.addElement(task.toString());
        }
    }

    private void showAddTaskDialog() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Название задачи:"));
        panel.add(nameField);
        panel.add(new JLabel("Описание (не обязательно):"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Добавить задачу",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            taskService.addTask(new TaskDto(nameField.getText(), descField.getText()));
            updateTasksList();
        }
    }

    private void toggleTaskStatus() {
        int selectedIndex = tasksList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = taskService.getAllTasks().get(selectedIndex);
            taskService.toggleTaskStatus(task.getId());
            updateTasksList();
        } else {
            JOptionPane.showMessageDialog(frame, "Выберите задачу для изменения статуса",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedIndex = tasksList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = taskService.getAllTasks().get(selectedIndex);
            taskService.deleteTask(task.getId());
            updateTasksList();
        } else {
            JOptionPane.showMessageDialog(frame, "Выберите задачу для удаления",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showEditTaskDialog() {
        int selectedIndex = tasksList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Выберите задачу для редактирования",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Task task = taskService.getAllTasks().get(selectedIndex);
        JTextField nameField = new JTextField(task.getName());
        JTextField descField = new JTextField(task.getDescription() != null ? task.getDescription() : "");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Название задачи:"));
        panel.add(nameField);
        panel.add(new JLabel("Описание:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Редактировать задачу",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            taskService.editTask(task.getId(),
                    nameField.getText(),
                    descField.getText().isEmpty() ? null : descField.getText());
            updateTasksList();
        }
    }

    private void showSearchDialog() {
        String keyword = JOptionPane.showInputDialog(frame, "Введите ключевое слово для поиска:", "Поиск задач", JOptionPane.PLAIN_MESSAGE);
        if (keyword != null && !keyword.isEmpty()) {
            List<Task> foundTasks = taskService.searchTasks(keyword);
            showTaskListInDialog(foundTasks, "Результаты поиска");
        }
    }

    private void showFilterDialog() {
        Object[] options = {"Выполненные", "Невыполненные"};
        int choice = JOptionPane.showOptionDialog(frame, "Фильтровать по:", "Фильтр задач",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice != JOptionPane.CLOSED_OPTION) {
            List<Task> filteredTasks = taskService.filterTasksByStatus(choice == 0);
            showTaskListInDialog(filteredTasks, choice == 0 ? "Выполненные задачи" : "Невыполненные задачи");
        }
    }

    private void showTaskListInDialog(List<Task> tasks, String title) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Task task : tasks) {
            model.addElement(task.toString());
        }

        JList<String> list = new JList<>(model);
        JOptionPane.showMessageDialog(frame, new JScrollPane(list), title, JOptionPane.PLAIN_MESSAGE);
    }

    private void saveTasks() {
        fileService.saveTasksToFile(taskService.getTasks());
        JOptionPane.showMessageDialog(frame, "Задачи успешно сохранены", "Сохранение", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskService taskService = new TaskService();
            FileService fileService = new FileService();
            new TodoSwingApp(taskService, fileService);
        });
    }
}