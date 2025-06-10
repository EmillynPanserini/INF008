package scr.manager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EventInputReader {

    public static EventCommonDetails collectCommonEventDetails() {
        String title = ValidInformation.readStringInput("Enter Title: ");

        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(ValidInformation.readStringInput("Enter Date (YYYY-MM-DD): "));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Insert again");
            }
        }

        String location = ValidInformation.readStringInput("Enter Location: ");

        int capacity = -1;
        while (capacity <= 0) {
            capacity = ValidInformation.readIntInput("Enter Capacity: ");
            if (capacity <= 0) {
                System.out.println("Capacity must be a positive number. Insert again");
            }
        }

        String description = ValidInformation.readStringInput("Enter Description: ");

        return new EventCommonDetails(title, date, location, capacity, description);
    }
}