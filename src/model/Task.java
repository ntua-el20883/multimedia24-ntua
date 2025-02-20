package model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents a Task with a title, description, category, priority,
 * deadline, and status.
 * <p>
 * Each Task also tracks a deadline date (in the format YYYY-MM-DD)
 * and a status (e.g. Open, In Progress, Completed, etc.).
 */
public class Task {

    private String title;
    private String description;
    private String category;
    private String priority;

    /**
     * The date by which this task should be completed, formatted as YYYY-MM-DD.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private String status;

    /**
     * Default constructor for JSON deserialization.
     */
    public Task() {
    }

    /**
     * Constructs a new Task with all required fields specified.
     *
     * @param title       The title of the task.
     * @param description A short description of the task.
     * @param category    The category under which this task falls.
     * @param priority    The priority level assigned to this task.
     * @param deadline    The completion deadline for this task.
     * @param status      The current status of the task (e.g., Open, Completed).
     */
    public Task(String title, String description, String category, String priority, LocalDate deadline, String status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }

    /**
     * Gets the task's title.
     *
     * @return The title of this task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the task's title.
     *
     * @param title The new title for this task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets a short description of this task.
     *
     * @return The description of this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this task.
     *
     * @param description A brief description of this task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the category of this task (e.g., Work, Personal).
     *
     * @return The task's category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category for this task.
     *
     * @param category The new category name.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the priority level of this task (e.g., High, Default).
     *
     * @return The task's priority level.
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority level for this task.
     *
     * @param priority The new priority name.
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Gets the deadline date for completing this task.
     *
     * @return The deadline as a {@link LocalDate}.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Sets the deadline date for this task.
     *
     * @param deadline The new deadline as a {@link LocalDate}.
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * Gets the current status of this task (e.g., Open, In Progress, Completed).
     *
     * @return The task status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status for this task.
     *
     * @param status The new status string (e.g., Completed).
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
