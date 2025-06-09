package ui;

import manager.*;
import scr.*;
import java.util.Scanner;

public class Menu {
    private EventManager eventManager;
    private Scanner sc;

    public Menu() {
        this.eventManager = new EventManager();
        this.sc = new Scanner(System.in);
    }

    public void run() {
        int option;
        do {
            printMenu();
            option = ValidInformation.readIntInput("Enter your choice: ");

            switch (option) {
                case 1:
                    addEvent();
                    break;
                case 2:
                    //implementar report Event
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
        sc.close();
    }

    private void printMenu() {
        System.out.println("\n--- Academic Events Manager ---");
        System.out.println("1. Add New Event");
        System.out.println("2. Report Event");
        System.out.println("0. Exit");
        System.out.println("-----------------------------");
    }


    private void addEvent() {
        System.out.println("\n--- Add New Event ---");
        System.out.println("\nSelect Event Type:");

        System.out.println("1. Academic Fair");
        System.out.println("2. Lecture");
        System.out.println("3. Short Course");
        System.out.println("4. Workshop");
        int type = ValidInformation.readIntInput("Enter event type (1-4): ");

        AcademicEvents newEvent = null;
        switch (type) {
            case 1:
                newEvent = new AcademicFair();
                break;
            case 2:
                Lecture speaker = ValidInformation.readStringInput("Enter lecture speaker: ");
                newEvent = new Lecture();
                break;
            case 3:
                newEvent = new ShortCourse();
                break;
            case 4:
                capacity = ValidInformation.readIntInput("Enter workshop capacity: ");
                newEvent = new Workshop();
                break;
            default:
                System.out.println("Invalid event type. Event not added.");
                return;
        }

        eventManager.addEvent(newEvent);
    }
}