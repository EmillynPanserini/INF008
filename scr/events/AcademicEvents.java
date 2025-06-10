package scr.events;

import java.time.LocalDate;

public abstract class AcademicEvents {
    private final String title;
    private final LocalDate date;
    private final String location;
    private final int capacity;
    private final String description;

    public AcademicEvents(String title, LocalDate date, String location, int capacity, String description) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required.");
        if (date == null) throw new IllegalArgumentException("Date is required.");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("Location is required.");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive.");
        if (description == null) description = "";

        this.title = title;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public abstract void displayDetails();

    @Override
    public String toString() {
        return "Event Title: " + title +
                ", Date: " + date +
                ", Location: " + location +
                ", Description: " + description +
                ", Capacity: " + capacity;
    }
}