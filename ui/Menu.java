package ui;

import manager.*;
import participants.*;
import scr.*;
import java.util.Scanner;

public class Menu {
    private EventManager eventManager;
    private Scanner sc;
    private int type;
    private ParticipantManager  participantManager;

    public Menu() {
        this.eventManager = new EventManager();
        this.participantManager = new ParticipantManager();
        this.sc = new Scanner(System.in);
    }

    void run() {
        int option;
        do {
            printMenu();
            option = ValidInformation.readIntInput("Enter your choice: ");

            switch (option) {
                case 1:
                    addEvent();
                    break;
                case 2:
                    System.out.println("Reporting events is not yet implemented.");
                    break;
                case 3:
                    addParticipant();
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

    void printMenu() {
        System.out.println("\n--- Academic Events Manager ---");
        System.out.println("1. Add New Event");
        System.out.println("2. Report Event");
        System.out.println("3. Add participant");
        System.out.println("0. Exit");
        System.out.println("-----------------------------");
    }

    void eventType(){
        System.out.println("\nSelect Event Type:");

        System.out.println("1. Academic Fair");
        System.out.println("2. Lecture");
        System.out.println("3. Short Course");
        System.out.println("4. Workshop");
    }


    void addEvent() {
        System.out.println("\n--- Add New Event ---");
        eventType();
        type = ValidInformation.readIntInput("Enter event type (1-4): ");

        AcademicEvents newEvent = null;
        AcademicEvents.EventCommonDetails commonDetails = AcademicEvents.collectCommonEventDetails();

        switch (type) {
            case 1:
                newEvent = new AcademicFair();
                break;
            case 2:
                newEvent = new Lecture();
                String speaker = ValidInformation.readStringInput("Enter lecture speaker: ");
                ((Lecture) newEvent).setSpeaker(speaker);
                break;
            case 3:
                newEvent = new ShortCourse();
                String instructorName = ValidInformation.readStringInput("Enter short course instructor: ");
                ((ShortCourse) newEvent).setInstructor(instructorName);
                break;
            case 4:
                newEvent = new Workshop();
                break;
            default:
                System.out.println("Invalid event type. Event not added.");
                return;
        }

        if (newEvent != null) {
            newEvent.setTitle(commonDetails.title());
            newEvent.setDate(commonDetails.date());
            newEvent.setLocation(commonDetails.location());
            newEvent.setCapacity(commonDetails.capacity());
            newEvent.setDescription(commonDetails.description());

            eventManager.addEvent(newEvent);
        }
    }
    void addParticipant() {
        System.out.println("\n--- Add New Participant ---");
        System.out.println("\nSelect Participant Type:");

        System.out.println("1. Student");
        System.out.println("2. Professor");
        System.out.println("3. External");
        type = ValidInformation.readIntInput("Enter participant type (1-3): ");

        String name = ValidInformation.readStringInput("Enter name: ");
        String email = ValidInformation.readStringInput("Enter email: ");

        Participant newParticipant = null;

        switch (type) {
            case 1:
                Student newStudent = new Student();
                String studentRegister = ValidInformation.readStringInput("Enter student register: ");
                newParticipant = new Student();
                newStudent.setRegistration(studentRegister);
                break;
            case 2:
                newParticipant = new Professor();
                break;
            case 3:
                newParticipant = new External();
                break;
            default:
                System.out.println("Invalid participant type. Participant not added.");
                return;
        }

        if (newParticipant != null) {
            newParticipant.setName(name);
            newParticipant.setEmail(email);
            participantManager.addParticipant(newParticipant);
        }

        /*
        eventType();
        type = ValidInformation.readIntInput("Enter event type (1-4): ");

         */
    }
}