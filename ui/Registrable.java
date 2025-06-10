package ui;

import participants.Participant;

public interface Registrable {
    boolean registerParticipant(Participant participant);
    int getAvailableSlots();
    int getTotalCapacity();
}
