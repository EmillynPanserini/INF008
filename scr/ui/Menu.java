package scr.ui;

import scr.events.*;
import scr.manager.*;
import scr.participants.*;
import scr.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Menu {
    private final EventManager eventManager;
    private final ParticipantManager participantManager;
    private final CertificateGenerator certificateGenerator;
    public Menu() {
        this.eventManager = new EventManager();
        this.participantManager = new ParticipantManager();
        this.certificateGenerator = new CertificateGenerator();
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
                    generateEventReport();
                    break;
                case 3:
                    addParticipant();
                    break;
                case 4:
                    if (EventManager.getAllEvents().isEmpty()) {
                        System.out.println("Register a event first.");
                        break;
                    }
                    participantManager.registerParticipantToEvent(participantManager.getParticipants());
                    break;
                case 5:
                    generateCertificateOption();
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
                2. Report Events (by Type/Date)
                3. Add participant
                4. Register Participant to Event
                5. Generate Certificate
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

        EventCommonDetails commonDetails = EventInputReader.collectCommonEventDetails();

        boolean isOnline = ValidInformation.readStringInput("Is this an online event? (yes/no): ").equalsIgnoreCase("yes");
        String platformUrl = null;

        String finalLocation = commonDetails.location(); //
        if (isOnline) {
            platformUrl = ValidInformation.readStringInput("Enter Platform URL: ");
            finalLocation = "Online Platform ";
        }


        switch (type) {
            case 1:
                newEvent = new AcademicFair(
                        commonDetails.title(), //
                        commonDetails.date(), //
                        finalLocation,
                        commonDetails.capacity(), //
                        commonDetails.description(), //
                        isOnline,
                        platformUrl
                );
                break;
            case 2:
                String speaker = ValidInformation.readStringInput("Enter lecture speaker: ");
                Lecture lecture = new Lecture(
                        commonDetails.title(), //
                        commonDetails.date(), //
                        finalLocation,
                        commonDetails.capacity(), //
                        commonDetails.description(), //
                        isOnline,
                        platformUrl
                );
                lecture.setSpeaker(speaker);
                newEvent = lecture;
                break;
            case 3:
                String instructorName = ValidInformation.readStringInput("Enter short course instructor: ");
                ShortCourse shortCourse = new ShortCourse(
                        commonDetails.title(), //
                        commonDetails.date(), //
                        finalLocation,
                        commonDetails.capacity(), //
                        commonDetails.description(), //
                        isOnline,
                        platformUrl
                );
                shortCourse.setInstructor(instructorName);
                newEvent = shortCourse;
                break;
            case 4:
                newEvent = new Workshop(
                        commonDetails.title(), //
                        commonDetails.date(), //
                        finalLocation,
                        commonDetails.capacity(), //
                        commonDetails.description(), //
                        isOnline,
                        platformUrl
                );
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
    private void generateEventReport() {
        System.out.println("\n--- Generate Event Report ---");

        String filterType = ValidInformation.readStringInput("Enter event type to filter (e.g., 'Fair', 'Lecture', 'ShortCourse', 'Workshop') or leave empty for any type: ");

        LocalDate filterDate = null;
        String dateInput = ValidInformation.readStringInput("Enter date to filter (YYYY-MM-DD) or leave empty for any date: ");
        if (!dateInput.isEmpty()) {
            try {
                filterDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Filtering by date will be skipped.");
            }
        }

        EventManager.reportEvent(filterType, filterDate);
    }
    private void generateCertificateOption() {
        System.out.println("\n--- Generate Certificate ---");

        if (EventManager.getAllEvents().isEmpty()) {
            System.out.println("No events registered to generate a certificate from.");
            return;
        }
        if (participantManager.getParticipants().isEmpty()) {
            System.out.println("No participants registered to generate a certificate for.");
            return;
        }
        EventManager.listAllEvents();
        int eventChoice = ValidInformation.readIntInput("Enter the number of the event for the certificate: ");
        AcademicEvents selectedEvent = null;
        if (eventChoice > 0 && eventChoice <= EventManager.getAllEvents().size()) {
            selectedEvent = EventManager.getAllEvents().get(eventChoice - 1);
        } else {
            System.out.println("Invalid event number.");
            return;
        }
        List<Participant> allParticipants = participantManager.getParticipants();
        System.out.println("\n--- Available Participants ---");
        for (int i = 0; i < allParticipants.size(); i++) {
            System.out.println((i + 1) + ". " + allParticipants.get(i).getName());
        }
        int participantChoice = ValidInformation.readIntInput("Enter the number of the participant for the certificate: ");
        Participant selectedParticipant = null;
        if (participantChoice > 0 && participantChoice <= allParticipants.size()) {
            selectedParticipant = allParticipants.get(participantChoice - 1);
        } else {
            System.out.println("Invalid participant number.");
            return;
        }

        if (selectedEvent != null && selectedParticipant != null) {
            certificateGenerator.generateCertificate(selectedEvent, selectedParticipant);
        } else {
            System.out.println("Could not generate certificate due to invalid selection.");
        }
    }
}