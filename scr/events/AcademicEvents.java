package scr.events;

import java.time.LocalDate;

public abstract class AcademicEvents {
    private final String title;
    private final LocalDate date;
    private final String location;
    private final int capacity;
    private final String description;
    private final boolean isOnline;
    private final String platformUrl;

    public AcademicEvents(String title, LocalDate date, String location, int capacity, String description, boolean isOnline, String platformUrl) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required.");
        if (date == null) throw new IllegalArgumentException("Date is required.");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("Location is required.");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive.");
        if (description == null) description = "";
        if (platformUrl == null || platformUrl.isBlank()) throw new IllegalArgumentException("Platform URL is required.");

        this.title = title;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.isOnline = isOnline;
        this.platformUrl = platformUrl;
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

    public boolean isOnline(){ return isOnline;}

    public String getPlatformUrl(){return platformUrl;}

    public abstract void displayDetails();

    @Override
    public String toString() {
        String baseString = "Event Title: " + title +
                ", Date: " + date +
                ", Location: " + location +
                ", Description: " + description +
                ", Capacity: " + capacity;
        if (isOnline){
            baseString += ", Platform URL " + platformUrl;
        }
        return baseString;
    }
}