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
    private Scanner sc;

    public EventManager(){
         this.sc = new Scanner(System.in);
    }

    void registerLecture (){

        System.out.println("\n--- Registering a New Lecture ---\n");

        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();

        System.out.print("Enter Speaker Name: ");
        String speaker = sc.nextLine();

        Lecture newLecture = new Lecture();
        this.events.add(newLecture);
        System.out.println("\nLecture \"" + commonDetails.title() + "\" registered!");
    }

    void registerWorkshop(){
        System.out.println("\n--- Registering a New Workshop ---\n");
        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();
        Workshop newWorkshop = new Workshop();
        this.events.add(newWorkshop);
        System.out.println("\nWorkshop \"" + commonDetails.title() + "\" registered!");
    }
}
