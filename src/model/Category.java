package model;

/**
 * Represents a category for grouping tasks.
 * <p>
 * Categories allow users to categorize tasks under different
 * labels like “Work,” “Personal,” etc.
 */
public class Category {
    private String name;

    /**
     * Default constructor for JSON deserialization.
     */
    public Category() {
    }

    /**
     * Constructs a new Category with the given name.
     *
     * @param name The name of the category.
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this category.
     *
     * @return The category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this category.
     *
     * @param name The new category name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of this category,
     * which is simply its name.
     *
     * @return The category name.
     */
    @Override
    public String toString() {
        return name;
    }
}
