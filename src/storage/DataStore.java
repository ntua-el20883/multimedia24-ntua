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

    private static final String TASKS_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/tasks.json";
    private static final String CATEGORIES_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/categories.json";
    private static final String PRIORITIES_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/priorities.json";
    private static final String REMINDERS_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/reminders.json";

    private DataStore() {
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        priorities = new ArrayList<>();
        reminders = new ArrayList<>();
        jsonHandler = new JSONHandler();
    }

    // Singleton pattern to ensure only one instance
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Load all data from JSON files
    public void loadAllData() {
        tasks = jsonHandler.loadTasks(TASKS_FILE);
        categories = jsonHandler.loadCategories(CATEGORIES_FILE);
        priorities = jsonHandler.loadPriorities(PRIORITIES_FILE);
        reminders = jsonHandler.loadReminders(REMINDERS_FILE);
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

    // Additional Methods for Category Management
    public void addCategory(Category category) {
        categories.add(category);
        saveCategories();
    }

    public void editCategory(Category oldCategory, String newName) {
        oldCategory.setName(newName);
        saveCategories();
    }

    public void deleteCategory(Category category) {
        categories.remove(category);
        saveCategories();
    }

    // Method to check if a category is in use
    public boolean isCategoryInUse(Category category) {
        for (Task task : tasks) {
            if (task.getCategory().equalsIgnoreCase(category.getName())) {
                return true;
            }
        }
        return false;
    }

    // Helper method to save categories (used by Category Management)
    private void saveCategories() {
        jsonHandler.saveCategories("medialab/categories.json", categories);
    }
}
