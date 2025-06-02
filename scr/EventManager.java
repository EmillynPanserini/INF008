package scr;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EventManager {
    private boolean validDate = false;
    private boolean validCapacity = false;
    private List<AcademicEvents> events;


    void registerEvents (){
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Registering a New Event ---\n");

        System.out.print("Enter the Title: ");
        String title = sc.nextLine();


        while (!validDate) {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String dateStr = sc.nextLine();
            try {
                LocalDate date = LocalDate.parse(dateStr);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.\n");
            }
        }

        System.out.print("Enter Location: ");
        String place = sc.nextLine();

        while (!validCapacity) {
            System.out.print("Enter Capacity: ");
            try {
                int capacity = sc.nextInt();
                if (capacity > 0) {
                    validCapacity = true;
                } else {
                    System.out.println("Capacity must be a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number for capacity.");
                sc.next();
            }
        }

        System.out.print("Enter Description: ");
        String description = sc.nextLine();

        //AcademicEvents newAcademicEvent = new AcademicEvents();


        //this.events.add(AcademicEvent);

        sc.close();
    }
}
