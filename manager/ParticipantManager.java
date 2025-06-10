package manager;

import participants.Participant;
import ui.Menu;

import java.util.ArrayList;
import java.util.List;

public class ParticipantManager {
    private List<Participant> participants;

    public ParticipantManager(){
        this.participants = new ArrayList<>();
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
}
