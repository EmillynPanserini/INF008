package scr.ui;

import scr.events.*;
import scr.manager.*;
import scr.participants.*;

import java.util.Scanner;

public class Menu {
    private EventManager eventManager;
    private int type;
    private ParticipantManager  participantManager;

    public Menu() {
        this.eventManager = new EventManager();
        this.participantManager = new ParticipantManager();
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
    }

    void printMenu() {
        final String menu = """
                --- Academic Events Manager ---
                1. Add New Event
                2. Report Event
                3. Add participant
                0. Exit
                -----------------------------
                """;
        System.out.println(menu);
    }
    void eventType() {
        final String eventType = """
                --- Select Event Type: ---
                1. Academic Fair
                2. Lecture
                3. Short Course
                0. Workshop
                -----------------------------
                """;
        System.out.println(eventType);
    }

    void addEvent() {
        System.out.println("\n--- Add New Event ---");
        eventType();
        type = ValidInformation.readIntInput("Enter event type (1-4): ");

        AcademicEvents newEvent = null;
        EventInputReader.collectCommonEventDetails() commonDetails = EventInputReader.collectCommonEventDetails();
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

        newEvent.setTitle(EventCommonDetails.title());
        newEvent.setDate(EventCommonDetails.date());
        newEvent.setLocation(EventCommonDetails.location());
        newEvent.setCapacity(EventCommonDetails.capacity());
        newEvent.setDescription(EventCommonDetails.description());
        eventManager.addEvent(newEvent);

    }
    void addParticipant() {
        final String participantMenu = """
                --- Add New Participant: ---
                --- Select Participant Type: ---
                1. Student
                2. Professor
                3. Short Course
                0. External
                -----------------------------
                """;
        System.out.println(participantMenu);

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


        newParticipant.setName(name);
        newParticipant.setEmail(email);
        participantManager.addParticipant(newParticipant);

    }
}