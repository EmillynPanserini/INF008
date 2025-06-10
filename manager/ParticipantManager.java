package manager;

import participants.Participant;
import scr.AcademicEvents;
import ui.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class ParticipantManager implements Registrable {
    private List<Participant> participants;
    private Scanner sc;
    private int registeredParticipantsCount = 0;

    public ParticipantManager(){
        this.participants = new ArrayList<>();
        this.sc = new Scanner(System.in);
    }
    public List<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant){
        if (participant != null){
            this.participants.add(participant);
            System.out.println("Participant: " + participant.getName());
        }
        else
            System.out.println("Cannot add.");

    }
    public void registerParticipantToEvent(List<Participant> allParticipants) {

        if (EventManager.getAllEvents().isEmpty()) {
            System.out.println("No events available to register participants.");
            return;
        }
        if (allParticipants.isEmpty()) {
            System.out.println("No participants available to register.");
            return;
        }

        System.out.println("\n--- Register Participant to Event ---");
        EventManager.listAllEvents();
        System.out.print("Enter the number of the event to register a participant: ");
        int eventIndex = -1;
        try {
            eventIndex = sc.nextInt();

            if (eventIndex > 0 && eventIndex <= EventManager.getAllEvents().size()) {
                AcademicEvents selectedEvent = EventManager.getAllEvents().get(eventIndex - 1);

                if (selectedEvent instanceof Registrable registrableEvent) {

                    System.out.println("\n--- Available Participants ---");
                    for (int x = 0; x < allParticipants.size(); x++) {
                        System.out.println((x + 1) + ". " + allParticipants.get(x).getName());
                    }
                    System.out.print("Enter the number of the participant to register: ");
                    int participantIndex = sc.nextInt();

                    if (participantIndex > 0 && participantIndex <= allParticipants.size()) {
                        Participant selectedParticipant = allParticipants.get(participantIndex - 1);

                        // call registerParticipant interface Registrable
                        if (registrableEvent.registerParticipant(selectedParticipant)) {
                            System.out.println("Participant registered successfully to " + selectedEvent.getTitle());
                        }
                    } else {
                        System.out.println("Invalid participant number.");
                    }

                } else {
                    System.out.println("This event type does not support participant registration.");
                }
            } else {
                System.out.println("Invalid event number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }


    /*@Override
    public boolean registerParticipant(Participant participant) {
        if (registeredParticipantsCount < getCapacity()) {
            registeredParticipantsCount++;
            System.out.println("Participant " + participant.getName() + " registrated: " + title);
            return true;
        } else {
            System.out.println("Full event: " + title);
            return false;
        }
    }*/
    @Override
    public boolean registerParticipant(Participant participant){return false;}

    @Override
    public int getAvailableSlots() {
        return 0;
    }

    @Override
    public int getTotalCapacity() {
        return 0;
    }
}
