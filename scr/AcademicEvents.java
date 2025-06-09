// scr/AcademicEvents.java
package scr;

import manager.ValidInformation; // Import ValidInformation to use its methods

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
// import java.util.InputMismatchException; // Not directly needed if ValidInformation handles it
// import java.util.Scanner; // Not directly needed if ValidInformation handles it

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
    public record EventCommonDetails(String title, LocalDate date, String location, int capacity,
                                     String description) {}

    public static EventCommonDetails collectCommonEventDetails() {

        String title = ValidInformation.readStringInput("Enter Title: ");

        LocalDate date = null;
        boolean validDate = false;
        while (!validDate) {
            String dateStr = ValidInformation.readStringInput("Enter Date (YYYY-MM-DD): ");
            try {
                date = LocalDate.parse(dateStr);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Insert again");
            }
        }

        String location = ValidInformation.readStringInput("Enter Location: ");

        boolean validCapacity = false;
        int capacity = 0;
        while (!validCapacity) {
            capacity = ValidInformation.readIntInput("Enter Capacity: ");
            if (capacity > 0) {
                validCapacity = true;
            } else {
                System.out.println("Capacity must be a positive number. Insert again");
            }
        }

        String description = ValidInformation.readStringInput("Enter Description: ");

        return new EventCommonDetails(title, date, location, capacity, description);
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