// manager/EventManager.java
package manager;

import scr.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EventManager {
    private List<AcademicEvents> events;

    public EventManager() {
        this.events = new ArrayList<>();
    }

    public void addEvent(AcademicEvents event) {
        if (event != null) {
            this.events.add(event);
            System.out.println("Event '" + event.getTitle() + "' added successfully.");
        } else {
            System.out.println("Cannot add a null event.");
        }
    }

    public List<AcademicEvents> getAllEvents() {
        return Collections.unmodifiableList(events);
    }


}