package scr;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;


abstract class AcademicEvents {
    protected  String title;
    protected LocalDate date = null;
    protected String location;
    protected int capacity = 0;
    protected String description;
    private static Scanner sc;

    public AcademicEvents(){
        sc = new Scanner(System.in);
    }
    protected record EventCommonDetails(String title, LocalDate date, String location, int capacity,
                                        String description) {}

    public static EventCommonDetails collectCommonEventDetails() {
        System.out.print("Enter Title: ");
        String title = sc.nextLine();

        LocalDate date = null;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String dateStr = sc.nextLine();
            try {
                date = LocalDate.parse(dateStr);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Insert again");
                sc.nextLine();
            }
        }

        System.out.print("Enter Location: ");
        String location = sc.nextLine();

        boolean validCapacity = false;
        int capacity = 0;
        while (!validCapacity) {
            System.out.print("Enter Capacity: ");
            try {
                capacity = sc.nextInt();
                if (capacity > 0) {
                    validCapacity = true;
                } else {
                    System.out.println("Capacity must be a positive number. Insert again");
                    sc.nextInt();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Insert again");
                sc.next();
            } finally {
                sc.nextLine();
            }
        }

        System.out.print("Enter Description: ");
        String description = sc.nextLine();

        return new EventCommonDetails(title, date, location, capacity, description);

    }

    abstract String getEventTypeDetails();

}
