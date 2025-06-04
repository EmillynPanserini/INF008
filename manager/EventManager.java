package manager;

import scr.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventManager {
    private List<AcademicEvents> events;
    private Scanner sc;

    public EventManager(){
         this.sc = new Scanner(System.in);
         this.events = new ArrayList<>();
    }

    public void registerLecture (){

        System.out.println("\n--- Registering a New Lecture ---\n");

        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();

        System.out.print("Enter Speaker Name: ");
        String speaker = sc.nextLine();

        Lecture newLecture = new Lecture();
        this.events.add(newLecture);
        System.out.println("\nLecture \"" + commonDetails.title() + "\" registered!");
    }

    public void registerWorkshop(){
        System.out.println("\n--- Registering a New Workshop ---\n");
        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();
        Workshop newWorkshop = new Workshop();
        this.events.add(newWorkshop);
        System.out.println("\nWorkshop \"" + commonDetails.title() + "\" registered!");
    }

    public void registerAcademicFair(){
        System.out.println("\n--- Registering a New Academic Fair ---\n");
        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();
        AcademicFair newAcademicFair = new AcademicFair();
        this.events.add(newAcademicFair);
        System.out.println("\nAcademic Fair \"" + commonDetails.title() + "\" registered!");
    }
    public void registerShortCourse(){
        System.out.println("\n--- Registering a New Short Course---\n");
        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();
        ShortCourse newShortCourse = new ShortCourse();
        this.events.add(newShortCourse);
        System.out.println("\nShort Course \"" + commonDetails.title() + "\" registered!");
    }

}
