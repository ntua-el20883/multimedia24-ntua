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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * A singleton class that manages the main data collections for tasks,
 * categories, priorities, and reminders.
 * <p>
 * Provides methods to load data from and save data to JSON files,
 * and includes logic to ensure a default category and priority.
 */
public class DataStore {

    private static DataStore instance = null;

    private List<Task> tasks;
    private List<Category> categories;
    private List<Priority> priorities;
    private List<Reminder> reminders;

    private JSONHandler jsonHandler;

    // File paths for JSON data
    private static final String TASKS_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/tasks.json";
    private static final String CATEGORIES_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/categories.json";
    private static final String PRIORITIES_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/priorities.json";
    private static final String REMINDERS_FILE = "C:/Users/juant/Documents/Java_Projects/multimedia24-ntua/medialab/reminders.json";

    /**
     * Private constructor for singleton usage. Initializes empty lists
     * and a {@link JSONHandler} for loading/saving data.
     */
    private DataStore() {
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        priorities = new ArrayList<>();
        reminders = new ArrayList<>();
        jsonHandler = new JSONHandler();
    }

    /**
     * Retrieves the singleton instance of DataStore.
     * If it doesn't exist yet, creates a new one.
     *
     * @return The singleton {@link DataStore} instance.
     */
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Loads all data (tasks, categories, priorities, reminders) from
     * their respective JSON files. Also ensures default category and priority.
     */
    public void loadAllData() {
        tasks = jsonHandler.loadTasks(TASKS_FILE);
        categories = jsonHandler.loadCategories(CATEGORIES_FILE);
        priorities = jsonHandler.loadPriorities(PRIORITIES_FILE);
        reminders = jsonHandler.loadReminders(REMINDERS_FILE);

        ensureDefaultCategory();
        ensureDefaultPriority();
    }

    /**
     * Saves all data (tasks, categories, priorities, reminders) to
     * their respective JSON files.
     */
    public void saveAllData() {
        jsonHandler.saveTasks(TASKS_FILE, tasks);
        jsonHandler.saveCategories(CATEGORIES_FILE, categories);
        jsonHandler.savePriorities(PRIORITIES_FILE, priorities);
        jsonHandler.saveReminders(REMINDERS_FILE, reminders);
    }

    // ========================
    // GETTERS
    // ========================

    /**
     * Returns the list of all tasks stored in memory.
     *
     * @return A {@link List} of {@link Task} objects.
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Returns the list of all categories stored in memory.
     *
     * @return A {@link List} of {@link Category} objects.
     */
    public List<Category> getAllCategories() {
        return categories;
    }

    /**
     * Returns the list of all priorities stored in memory.
     *
     * @return A {@link List} of {@link Priority} objects.
     */
    public List<Priority> getAllPriorities() {
        return priorities;
    }

    /**
     * Returns the list of all reminders stored in memory.
     *
     * @return A {@link List} of {@link Reminder} objects.
     */
    public List<Reminder> getAllReminders() {
        return reminders;
    }

    // ========================
    // TASK METHODS
    // ========================

    /**
     * Checks whether a given {@link Task} is due within a specified number of days.
     *
     * @param task The task to check.
     * @param days The number of days within which the task is considered 'due'.
     * @return True if the task's deadline is within the specified days from now;
     *         false otherwise.
     */
    public boolean isTaskDueInDays(Task task, int days) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = task.getDeadline();
        long daysBetween = ChronoUnit.DAYS.between(today, deadline);
        return daysBetween >= 0 && daysBetween <= days;
    }

    /**
     * Saves the current list of tasks to the {@code TASKS_FILE}.
     * Used internally after modifying tasks in memory.
     */
    private void saveTasks() {
        jsonHandler.saveTasks(TASKS_FILE, tasks);
    }

    // ========================
    // CATEGORY METHODS
    // ========================

    /**
     * Ensures a category named "Default" exists in memory. If not,
     * it is created and saved.
     */
    private void ensureDefaultCategory() {
        boolean hasDefaultCategory = categories.stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase("Default"));
        if (!hasDefaultCategory) {
            categories.add(new Category("Default"));
            saveCategories();
        }
    }

    /**
     * Adds a new category (except "Default", which is reserved).
     * Updates categories.json after adding.
     *
     * @param category The {@link Category} object to be added.
     * @throws IllegalArgumentException If the category name is "Default".
     */
    public void addCategory(Category category) {
        if (category.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' category is reserved and cannot be manually created.");
        }
        categories.add(category);
        saveCategories();
    }

    /**
     * Edits an existing category's name. Prevents renaming of "Default".
     * Also updates tasks that reference this category.
     *
     * @param oldCategory The current category object.
     * @param newName     The new name to assign to this category.
     * @throws IllegalArgumentException If the category is "Default" or if a
     *                                  duplicate name exists.
     */
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

        // Update tasks referencing the old category
        tasks.forEach(task -> {
            if (task.getCategory().equalsIgnoreCase(oldCategory.getName())) {
                task.setCategory(newName);
            }
        });

        oldCategory.setName(newName);
        saveCategories();
        saveTasks();
    }

    /**
     * Deletes a category (except "Default") and removes tasks assigned
     * to that category from memory.
     *
     * @param category The category to delete.
     * @throws IllegalArgumentException If attempting to delete "Default".
     */
    public void deleteCategory(Category category) {
        if (category.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' category cannot be deleted.");
        }

        categories.remove(category);

        // Remove tasks associated with the deleted category
        tasks.removeIf(task -> task.getCategory().equalsIgnoreCase(category.getName()));

        saveCategories();
        saveTasks();
    }

    /**
     * Checks if a given category is in use by any task.
     *
     * @param category The category to verify.
     * @return True if at least one task references this category; false otherwise.
     */
    public boolean isCategoryInUse(Category category) {
        for (Task task : tasks) {
            if (task.getCategory().equalsIgnoreCase(category.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves the current list of categories to the {@code CATEGORIES_FILE}.
     * Used internally after modifying categories in memory.
     */
    private void saveCategories() {
        jsonHandler.saveCategories(CATEGORIES_FILE, categories);
    }

    // ========================
    // PRIORITY METHODS
    // ========================

    /**
     * Adds a new priority to the list, ensuring no duplicates.
     * Saves after adding.
     *
     * @param priority The new {@link Priority} to add.
     * @throws IllegalArgumentException If a priority with the same name already
     *                                  exists.
     */
    public void addPriority(Priority priority) {
        boolean exists = priorities.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(priority.getName()));
        if (exists) {
            throw new IllegalArgumentException("A priority with the same name already exists.");
        }

        priorities.add(priority);
        savePriorities();
    }

    /**
     * Edits the name of an existing priority. Prevents renaming of "Default".
     * Also updates tasks using this priority.
     *
     * @param oldPriority The priority to rename.
     * @param newName     The new name for the priority.
     * @throws IllegalArgumentException If the priority is "Default" or if a
     *                                  duplicate name exists.
     */
    public void editPriority(Priority oldPriority, String newName) {
        if (oldPriority.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' priority cannot be edited.");
        }

        boolean exists = priorities.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(newName) && p != oldPriority);
        if (exists) {
            throw new IllegalArgumentException("A priority with the same name already exists.");
        }

        tasks.forEach(task -> {
            if (task.getPriority().equalsIgnoreCase(oldPriority.getName())) {
                task.setPriority(newName);
            }
        });

        oldPriority.setName(newName);
        savePriorities();
        saveTasks();
    }

    /**
     * Deletes the specified priority (except "Default") and reassigns any tasks
     * using that priority to "Default".
     *
     * @param priority The priority to delete.
     * @throws IllegalArgumentException If attempting to delete "Default".
     */
    public void deletePriority(Priority priority) {
        if (priority.getName().equalsIgnoreCase("Default")) {
            throw new IllegalArgumentException("The 'Default' priority cannot be deleted.");
        }

        // Reassign tasks that use the deleted priority to "Default"
        for (Task task : tasks) {
            if (task.getPriority().equalsIgnoreCase(priority.getName())) {
                task.setPriority("Default");
            }
        }

        priorities.remove(priority);

        savePriorities();
        saveTasks();
    }

    /**
     * Ensures the 'Default' priority exists in memory. If not, adds it and saves.
     */
    private void ensureDefaultPriority() {
        boolean hasDefaultPriority = priorities.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase("Default"));
        if (!hasDefaultPriority) {
            priorities.add(new Priority("Default"));
            savePriorities();
        }
    }

    /**
     * Saves the current list of priorities to the {@code PRIORITIES_FILE}.
     * Used internally after modifying priorities in memory.
     */
    private void savePriorities() {
        jsonHandler.savePriorities(PRIORITIES_FILE, priorities);
    }

    // ========================
    // REMINDER METHODS
    // ========================

    /**
     * Adds a new reminder. Ensures the task exists and avoids duplicate reminders
     * for the same task on the same date.
     *
     * @param reminder The new {@link Reminder} to add.
     * @throws IllegalArgumentException If the referenced task doesn't exist or if a
     *                                  duplicate reminder is found.
     */
    public void addReminder(Reminder reminder) {
        // Check if the task exists
        boolean taskExists = tasks.stream()
                .anyMatch(task -> task.getTitle().equalsIgnoreCase(reminder.getTaskTitle()));
        if (!taskExists) {
            throw new IllegalArgumentException("No task found with the title: " + reminder.getTaskTitle());
        }

        // Check for duplicate reminders
        boolean exists = reminders.stream()
                .anyMatch(r -> r.getTaskTitle().equalsIgnoreCase(reminder.getTaskTitle()) &&
                        r.getDate().equals(reminder.getDate()));
        if (exists) {
            throw new IllegalArgumentException("A reminder for this task on the selected date already exists.");
        }

        reminders.add(reminder);
        saveReminders();
    }

    /**
     * Edits an existing reminder by replacing old data with new reminder data.
     * Ensures the task exists and that no duplicate reminder is created.
     *
     * @param oldReminder The existing reminder to update.
     * @param newReminder The new reminder data.
     * @throws IllegalArgumentException If the task doesn't exist or a duplicate
     *                                  reminder date is found.
     */
    public void editReminder(Reminder oldReminder, Reminder newReminder) {
        // Check if the task exists
        boolean taskExists = tasks.stream()
                .anyMatch(task -> task.getTitle().equalsIgnoreCase(newReminder.getTaskTitle()));
        if (!taskExists) {
            throw new IllegalArgumentException("No task found with the title: " + newReminder.getTaskTitle());
        }

        // Check for duplicate reminders (excluding the one being edited)
        boolean exists = reminders.stream()
                .anyMatch(r -> r.getTaskTitle().equalsIgnoreCase(newReminder.getTaskTitle()) &&
                        r.getDate().equals(newReminder.getDate()) &&
                        r != oldReminder);
        if (exists) {
            throw new IllegalArgumentException("A reminder for this task on the selected date already exists.");
        }

        // Update the reminder
        oldReminder.setTaskTitle(newReminder.getTaskTitle());
        oldReminder.setDate(newReminder.getDate());
        saveReminders();
    }

    /**
     * Deletes the specified reminder from the list and saves the updated reminders.
     *
     * @param reminder The {@link Reminder} object to remove.
     */
    public void deleteReminder(Reminder reminder) {
        reminders.remove(reminder);
        saveReminders();
    }

    /**
     * Saves the current list of reminders to the {@code REMINDERS_FILE}.
     * Used internally after modifying reminders in memory.
     */
    private void saveReminders() {
        jsonHandler.saveReminders(REMINDERS_FILE, reminders);
    }
}
