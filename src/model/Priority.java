package model;

/**
 * Represents a priority level for tasks (e.g., High, Medium, Low).
 * <p>
 * Users can define custom priority levels, excluding the default one.
 */
public class Priority {
    private String name;

    /**
     * Default constructor for JSON deserialization.
     */
    public Priority() {
    }

    /**
     * Constructs a new Priority with the specified name.
     *
     * @param name The name of the priority level.
     */
    public Priority(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this priority.
     *
     * @return The priority name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this priority.
     *
     * @param name The new priority name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the priority name as a string.
     *
     * @return The name of this priority.
     */
    @Override
    public String toString() {
        return name;
    }
}
