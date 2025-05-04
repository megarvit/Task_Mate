package com.taskmate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    private static final String DATA_FILE = "tasks.json";
    private final Gson gson;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void updateTask(Task task) {
        saveTasks();
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        saveTasks();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByPriority(Task.Priority priority) {
        return tasks.stream()
            .filter(task -> task.getPriority() == priority)
            .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(Task.Status status) {
        return tasks.stream()
            .filter(task -> task.getStatus() == status)
            .collect(Collectors.toList());
    }

    public List<Task> searchTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
            .filter(task -> task.getTitle().toLowerCase().contains(lowerKeyword) ||
                          task.getDescription().toLowerCase().contains(lowerKeyword))
            .collect(Collectors.toList());
    }

    private void saveTasks() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                tasks = gson.fromJson(reader, new TypeToken<List<Task>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} 