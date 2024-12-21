package model;

public class Category {
    private String name;

    // Default constructor for JSON deserialization
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
