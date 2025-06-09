package manager;

import scr.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EventManager {
    private List<AcademicEvents> events;
    private Scanner sc;

    public EventManager() {
        this.events = new ArrayList<>();
    }

    public void addEvent(AcademicEvents event) {
        if (event != null) {
            this.events.add(event);
            System.out.println("Event '" + event.getTitle() + "' added successfully.");
        } else {
            System.out.println("Cannot add a null event.");
        }
    }
    public void addEvent (String Speaker){

        System.out.println("\n--- Registering a New Lecture ---\n");
        Lecture newLecture = new Lecture();

        System.out.print("Enter Speaker Name: ");
        Lecture.setSpeaker();

        this.events.add(newLecture);
    }

    public void registerWorkshop(){
        System.out.println("\n--- Registering a New Workshop ---\n");
        Workshop newWorkshop = new Workshop();
        this.events.add(newWorkshop);
    }

    public void registerAcademicFair(){
        System.out.println("\n--- Registering a New Academic Fair ---\n");
        AcademicFair newAcademicFair = new AcademicFair();
        this.events.add(newAcademicFair);
    }
    public void registerShortCourse(){
        System.out.println("\n--- Registering a New Short Course---\n");
        ShortCourse newShortCourse = new ShortCourse();
        this.events.add(newShortCourse);
    }

    public List<AcademicEvents> getAllEvents() {
        return Collections.unmodifiableList(events);
    }

// achar por data e titulo
public record EventCommonDetails(String title, LocalDate date, String location, int capacity,
                                 String description) {}

    public static EventCommonDetails collectCommonEventDetails() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Title: ");
         title = sc.();


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
        private AcademicEvents location = sc.nextLine();

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