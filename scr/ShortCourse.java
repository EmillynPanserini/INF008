package scr;

import participants.Participant;
import ui.Registrable;

public class ShortCourse extends AcademicEvents implements Registrable {
    private String instructor;
    private int registeredParticipantsCount = 0;

    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public void displayDetails() {
        System.out.println("Title: " + getTitle());
        System.out.println("Date: " + getDate());
        System.out.println("Location: " + getLocation());
        System.out.println("Description: " + getDescription());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Instructor: " + instructor);
        System.out.println("----------------------------");
    }
    @Override
    public String toString() {
        return super.toString() +
                ", Instructor: " + instructor;
    }

    @Override
    public boolean registerParticipant(Participant participant) {
        if (registeredParticipantsCount < getCapacity()) {
            registeredParticipantsCount++;
            System.out.println("Participant " + participant.getName() + " registered: " + getTitle());
            return true;
        } else {
            System.out.println("Full event: " + getTitle());
            return false;
        }
    }

    @Override
    public int getAvailableSlots() {
        return getCapacity() - registeredParticipantsCount;
    }

    @Override
    public int getTotalCapacity() {
        return getCapacity();
    }
}