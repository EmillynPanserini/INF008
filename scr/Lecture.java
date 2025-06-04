package scr;

import java.util.ArrayList;

public class Lecture extends AcademicEvents {
    private String speaker;

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    @Override
    String getEventTypeDetails() {
        return "Lecture given by" + speaker;
    }


}
