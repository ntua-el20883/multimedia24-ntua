package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Task;
import model.Category;
import model.Priority;
import model.Reminder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * A utility class for loading and saving tasks, categories, priorities,
 * and reminders in JSON format. Leverages the Jackson library to
 * serialize/deserialize data structures.
 */
public class JSONHandler {
    /**
     * The Jackson ObjectMapper used for serialization and deserialization.
     */
    private ObjectMapper objectMapper;

    /**
     * Constructs a new JSONHandler by creating an {@link ObjectMapper} instance,
     * registering the {@link JavaTimeModule} for date/time handling,
     * and disabling timestamps for dates.
     */
    public JSONHandler() {
        objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        // Disable writing dates as timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Loads a list of {@link Task} objects from the specified JSON file.
     *
     * @param filePath The path to the tasks JSON file.
     * @return A list of tasks loaded from the file, or an empty list if not found
     *         or on error.
     */
    public List<Task> loadTasks(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("Loading tasks from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Task>>() {
                });
            } else {
                System.out.println("Tasks file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Saves a list of {@link Task} objects to the specified JSON file.
     *
     * @param filePath The path to the tasks JSON file.
     * @param tasks    The list of tasks to save.
     */
    public void saveTasks(String filePath, List<Task> tasks) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), tasks);
            System.out.println("Tasks saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving tasks to: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Loads a list of {@link Category} objects from the specified JSON file.
     *
     * @param filePath The path to the categories JSON file.
     * @return A list of categories loaded from the file, or an empty list if not
     *         found or on error.
     */
    public List<Category> loadCategories(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("Loading categories from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Category>>() {
                });
            } else {
                System.out.println("Categories file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading categories from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Saves a list of {@link Category} objects to the specified JSON file.
     *
     * @param filePath   The path to the categories JSON file.
     * @param categories The list of categories to save.
     */
    public void saveCategories(String filePath, List<Category> categories) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), categories);
            System.out.println("Categories saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving categories to: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Loads a list of {@link Priority} objects from the specified JSON file.
     *
     * @param filePath The path to the priorities JSON file.
     * @return A list of priorities loaded from the file, or an empty list if not
     *         found or on error.
     */
    public List<Priority> loadPriorities(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("Loading priorities from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Priority>>() {
                });
            } else {
                System.out.println("Priorities file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading priorities from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Saves a list of {@link Priority} objects to the specified JSON file.
     *
     * @param filePath   The path to the priorities JSON file.
     * @param priorities The list of priorities to save.
     */
    public void savePriorities(String filePath, List<Priority> priorities) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), priorities);
            System.out.println("Priorities saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving priorities to: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Loads a list of {@link Reminder} objects from the specified JSON file.
     *
     * @param filePath The path to the reminders JSON file.
     * @return A list of reminders loaded from the file, or an empty list if not
     *         found or on error.
     */
    public List<Reminder> loadReminders(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("Loading reminders from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Reminder>>() {
                });
            } else {
                System.out.println("Reminders file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading reminders from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Saves a list of {@link Reminder} objects to the specified JSON file.
     *
     * @param filePath  The path to the reminders JSON file.
     * @param reminders The list of reminders to save.
     */
    public void saveReminders(String filePath, List<Reminder> reminders) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), reminders);
            System.out.println("Reminders saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving reminders to: " + filePath);
            e.printStackTrace();
        }
    }
}
