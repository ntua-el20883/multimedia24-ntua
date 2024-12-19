package model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Reminder {
    private String taskTitle; // Reference by Task title
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    // Default constructor for Jackson
    public Reminder() {}

    public Reminder(String taskTitle, LocalDate date) {
        this.taskTitle = taskTitle;
        this.date = date;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
