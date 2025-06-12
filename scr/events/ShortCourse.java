package scr.events;

import scr.participants.*;
import scr.ui.Registrable;
import java.time.LocalDate;

public class ShortCourse extends AcademicEvents implements Registrable {
    private String instructor;
    private int registeredParticipantsCount = 0;

    public ShortCourse(String title, LocalDate date, String location, int capacity, String description,  boolean isOnline, String platformUrl) {
        super(title, date, location, capacity, description, isOnline, platformUrl);
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public void displayDetails() {
        System.out.println("Title: " + getTitle());
        System.out.println("Date: " + getDate());
        System.out.println("Location: " + getLocation());
        if (isOnline()) {
            System.out.println("Platform URL: " + getPlatformUrl());
        }
        System.out.println("Description: " + getDescription());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Available Slots: " + getAvailableSlots());
        System.out.println("Instructor: " + instructor);
        System.out.println("----------------------------");
    }
    @Override
    public String toString() {
        return super.toString() +
                ", Instructor: " + instructor +
                ", Available Slots: " + getAvailableSlots() +
                " / Capacity: " + getTotalCapacity();
    }

    @Override
    public boolean registerParticipant(Participant participant) {
        if (!(participant instanceof Student)) {
            System.out.println("Only students can do Short Courses.");
            return false;
        }

        if (getAvailableSlots() > 0) {
            registeredParticipantsCount++;
            System.out.println("Participant " + participant.getName() + " registrared: " + getTitle());
            return true;
        } else {
            System.out.println("The event is full: " + getTitle());
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