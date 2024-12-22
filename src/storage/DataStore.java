package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import model.Task;
import model.Category;
import model.Priority;
import model.Reminder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type; // Added import
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
    // Load all data from JSON files
    public void loadAllData() {
        tasks = jsonHandler.loadTasks(TASKS_FILE);
        categories = jsonHandler.loadCategories(CATEGORIES_FILE);
        priorities = jsonHandler.loadPriorities(PRIORITIES_FILE);
        reminders = jsonHandler.loadReminders(REMINDERS_FILE);

        // Ensure the "Default" category exists
        ensureDefaultCategory();
    }

    // Ensure the "Default" category exists in the list
    private void ensureDefaultCategory() {
        boolean hasDefaultCategory = categories.stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase("Default"));
        if (!hasDefaultCategory) {
            categories.add(new Category("Default"));
            saveCategories();
        }
    }

    // Save all data to JSON files
    public void saveAllData() {
        jsonHandler.saveTasks(TASKS_FILE, tasks);
        jsonHandler.saveCategories(CATEGORIES_FILE, categories);
        jsonHandler.savePriorities(PRIORITIES_FILE, priorities);
        jsonHandler.saveReminders(REMINDERS_FILE, reminders);
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
        if (category.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' category is reserved and cannot be manually created.");
        }
        categories.add(category);
        saveCategories();
    }

    // Ensure the "Default" category exists in loadCategories
    private void loadCategories() {
        try (Reader reader = new FileReader(CATEGORIES_FILE)) {
            Type categoryListType = new TypeReference<List<Category>>() {
            }.getType();
            List<Category> loadedCategories = jsonHandler.loadCategories(CATEGORIES_FILE);
            if (loadedCategories != null) {

                categories = loadedCategories;
            }
        } catch (IOException e) {
            // Initialize with default categories if file is missing
            categories = new ArrayList<>();
        }

        // Ensure the "Default" category exists
        boolean hasDefaultCategory = categories.stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase("Default"));
        if (!hasDefaultCategory) {
            categories.add(new Category("Default"));
            saveCategories();
        }
    }

    public void editCategory(Category oldCategory, String newName) {
        if (oldCategory.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' category cannot be renamed.");
        }

        // Check for duplicate category names
        boolean exists = categories.stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase(newName) && cat != oldCategory);
        if (exists) {
            throw new IllegalArgumentException("A category with this name already exists.");
        }

        // Update tasks with the old category name to the new category name
        tasks.forEach(task -> {
            if (task.getCategory().equalsIgnoreCase(oldCategory.getName())) {
                task.setCategory(newName);
            }
        });

        // Update the category name
        oldCategory.setName(newName);
        saveCategories();
        saveTasks();
    }

    public void deleteCategory(Category category) {
        if (category.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' category cannot be deleted.");
        }

        // Remove the category
        categories.remove(category);

        // Remove tasks associated with the deleted category
        tasks.removeIf(task -> task.getCategory().equalsIgnoreCase(category.getName()));

        saveCategories();
        saveTasks();
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
        jsonHandler.saveCategories(CATEGORIES_FILE, categories);
    }

    // Helper method to save tasks
    private void saveTasks() {
        jsonHandler.saveTasks(TASKS_FILE, tasks);
    }
}
