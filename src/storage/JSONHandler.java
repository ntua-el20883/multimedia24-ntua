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

public class JSONHandler {

    private ObjectMapper objectMapper;

    public JSONHandler() {
        objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        // Disable writing dates as timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Load tasks from JSON
    public List<Task> loadTasks(String filePath) {
        try {
            File file = new File(filePath);
            if(file.exists()) {
                System.out.println("Loading tasks from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Task>>(){});
            } else {
                System.out.println("Tasks file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Save tasks to JSON
    public void saveTasks(String filePath, List<Task> tasks) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), tasks);
            System.out.println("Tasks saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving tasks to: " + filePath);
            e.printStackTrace();
        }
    }

    // Load categories from JSON
    public List<Category> loadCategories(String filePath) {
        try {
            File file = new File(filePath);
            if(file.exists()) {
                System.out.println("Loading categories from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Category>>(){});
            } else {
                System.out.println("Categories file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading categories from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Save categories to JSON
    public void saveCategories(String filePath, List<Category> categories) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), categories);
            System.out.println("Categories saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving categories to: " + filePath);
            e.printStackTrace();
        }
    }

    // Load priorities from JSON
    public List<Priority> loadPriorities(String filePath) {
        try {
            File file = new File(filePath);
            if(file.exists()) {
                System.out.println("Loading priorities from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Priority>>(){});
            } else {
                System.out.println("Priorities file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading priorities from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Save priorities to JSON
    public void savePriorities(String filePath, List<Priority> priorities) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), priorities);
            System.out.println("Priorities saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving priorities to: " + filePath);
            e.printStackTrace();
        }
    }

    // Load reminders from JSON
    public List<Reminder> loadReminders(String filePath) {
        try {
            File file = new File(filePath);
            if(file.exists()) {
                System.out.println("Loading reminders from: " + file.getAbsolutePath());
                return objectMapper.readValue(file, new TypeReference<List<Reminder>>(){});
            } else {
                System.out.println("Reminders file not found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error loading reminders from: " + filePath);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Save reminders to JSON
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
