package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import model.Task;
import model.Category;
import model.Priority;
import model.Reminder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DataStore {

    private static DataStore instance = null;

    private List<Task> tasks;
    private List<Category> categories;
    private List<Priority> priorities;
    private List<Reminder> reminders;

    private JSONHandler jsonHandler;

    private DataStore() {
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        priorities = new ArrayList<>();
        reminders = new ArrayList<>();
        jsonHandler = new JSONHandler();
    }

    // Singleton pattern to ensure only one instance
    public static DataStore getInstance() {
        if(instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Load all data from JSON files
    public void loadAllData() {
        tasks = jsonHandler.loadTasks("C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/tasks.json");
        categories = jsonHandler.loadCategories("C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/categories.json");
        priorities = jsonHandler.loadPriorities("C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/priorities.json");
        reminders = jsonHandler.loadReminders("C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/reminders.json");
    }    

    // Save all data to JSON files
    public void saveAllData() {
        jsonHandler.saveTasks("medialab/tasks.json", tasks);
        jsonHandler.saveCategories("medialab/categories.json", categories);
        jsonHandler.savePriorities("medialab/priorities.json", priorities);
        jsonHandler.saveReminders("medialab/reminders.json", reminders);
    }

    // Getters
    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public List<Priority> getAllPriorities() {
        return priorities;
    }

    public List<Reminder> getAllReminders() {
        return reminders;
    }

    // Check if a task is due within a certain number of days
    public boolean isTaskDueInDays(Task task, int days) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = task.getDeadline();
        long daysBetween = ChronoUnit.DAYS.between(today, deadline);
        return daysBetween >= 0 && daysBetween <= days;
    }

    // Additional CRUD operations can be added here
}
