package scr.events;

import scr.participants.Participant;
import scr.ui.Registrable;
import java.time.LocalDate;

public class OnlineEvent extends AcademicEvents implements Registrable {
    private int registerParticipantsCount = 0;

    public OnlineEvent(String title, LocalDate date, String location, int capacity, String description) {
        super(title, date, location, capacity, description);
    }

    @Override
    public void displayDetails() {

    }

    @Override
    public boolean registerParticipant(Participant participant) {
        if (registerParticipantsCount < getCapacity()) {
            registerParticipantsCount++;
            System.out.println("Participant " + participant.getName() + " registrado no evento online: " + getTitle());
            return true;
        } else {
            System.out.println("Full event online : " + getTitle());
            return false;
        }
    }

    @Override
    public int getAvailableSlots() {
        return 0;
    }

    @Override
    public int getTotalCapacity() {
        return 0;
    }
}
