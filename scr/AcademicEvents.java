package scr;

import java.time.LocalDate;


public abstract class AcademicEvents {
    private   String title;
    private  LocalDate date = null;
    private  String location;
    private  int capacity;
    private  String description;

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
    public int getCapacity() {
        return capacity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
