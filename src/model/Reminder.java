package model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents a reminder for a specific task.
 * <p>
 * A reminder is associated with a particular task title and a date
 * indicating when the user should be notified (e.g., one day before
 * the task deadline).
 */
public class Reminder {
    /**
     * The title of the task that this reminder references.
     */
    private String taskTitle;

    /**
     * The date on which the reminder is set.
     * This is stored in the format YYYY-MM-DD.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * Default constructor for JSON deserialization.
     */
    public Reminder() {
    }

    /**
     * Constructs a new Reminder with the given task title and date.
     *
     * @param taskTitle The title of the associated task.
     * @param date      The date for the reminder.
     */
    public Reminder(String taskTitle, LocalDate date) {
        this.taskTitle = taskTitle;
        this.date = date;
    }

    /**
     * Gets the task title associated with this reminder.
     *
     * @return The task title.
     */
    public String getTaskTitle() {
        return taskTitle;
    }

    /**
     * Sets the task title for this reminder.
     *
     * @param taskTitle The new task title.
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    /**
     * Gets the date of this reminder.
     *
     * @return The reminder date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of this reminder.
     *
     * @param date The new reminder date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
