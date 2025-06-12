package scr.ui;

import scr.events.*;
import scr.manager.*;
import scr.participants.*;
import java.time.LocalDate;

public class Menu {
    private EventManager eventManager;
    private ParticipantManager participantManager;
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
                    // implementar report event
                    break;
                case 3:
                    addParticipant();
                    break;
                case 4:
                    participantManager.registerParticipantToEvent(participantManager.getParticipants());
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
                4. Register Participant to Event
                0. Exit
                -----------------------------
                """;
        System.out.println(menu);
    }


    void eventTypeMenu() {
        final String eventType = """
                --- Select Event Type: ---
                1. Academic Fair
                2. Lecture
                3. Short Course
                4. Workshop
                -----------------------------
                """;
        System.out.println(eventType);
    }

    void addEvent() {
        System.out.println("\n--- Add New Event ---");
        eventTypeMenu();
        int type = ValidInformation.readIntInput("Enter event type (1-4): ");

        AcademicEvents newEvent = null;

        String title = ValidInformation.readStringInput("Enter Title: ");
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(ValidInformation.readStringInput("Enter Date (YYYY-MM-DD): "));
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date format. Insert again");
            }
        }
        String description = ValidInformation.readStringInput("Enter Description: ");
        int capacity = -1;
        while (capacity <= 0) {
            capacity = ValidInformation.readIntInput("Enter Capacity: ");
            if (capacity <= 0) {
                System.out.println("Capacity must be a positive number. Insert again");
            }
        }


        boolean isOnline = ValidInformation.readStringInput("Is this an online event? (yes/no): ").equalsIgnoreCase("yes");
        String location;
        String platformUrl = null;

        if (isOnline) {
            platformUrl = ValidInformation.readStringInput("Enter Platform URL: ");
            location = "Online Platform ";
        } else {
            location = ValidInformation.readStringInput("Enter Location: ");
        }


        switch (type) {
            case 1:
                newEvent = new AcademicFair(title, date, location, capacity, description, isOnline, platformUrl);
                break;
            case 2:
                String speaker = ValidInformation.readStringInput("Enter lecture speaker: ");
                Lecture lecture = new Lecture(title, date, location, capacity, description, isOnline, platformUrl);
                lecture.setSpeaker(speaker);
                newEvent = lecture;
                break;
            case 3:
                String instructorName = ValidInformation.readStringInput("Enter short course instructor: ");
                ShortCourse shortCourse = new ShortCourse(title, date, location, capacity, description, isOnline, platformUrl);
                shortCourse.setInstructor(instructorName);
                newEvent = shortCourse;
                break;
            case 4:
                newEvent = new Workshop(title, date, location, capacity, description, isOnline, platformUrl);
                break;
            default:
                System.out.println("Invalid event type. Event not added.");
                return;
        }
        eventManager.addEvent(newEvent);
    }

    void addParticipant() {
        final String participantMenu = """
                --- Add New Participant: ---
                --- Select Participant Type: ---
                1. Student
                2. Professor
                3. External
                -----------------------------
                """;
        System.out.println(participantMenu);

        int type = ValidInformation.readIntInput("Enter participant type (1-3): ");

        String name = ValidInformation.readStringInput("Enter name: ");
        String email = ValidInformation.readStringInput("Enter email: ");

        Participant newParticipant = null;

        switch (type) {
            case 1:
                Student student = new Student();
                String studentRegister = ValidInformation.readStringInput("Enter student registration: ");
                student.setRegistration(studentRegister);
                newParticipant = student;
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