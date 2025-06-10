package scr.ui;

import scr.participants.Participant;

public interface Registrable {
    boolean registerParticipant(Participant participant);
    int getAvailableSlots();
    int getTotalCapacity();
}
