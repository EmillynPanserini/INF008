// manager/EventManager.java
package manager;

import scr.*;
import java.util.*;

public class EventManager {
    private static List<AcademicEvents> events;

    public EventManager() {
        events = new ArrayList<>();
    }

    public void addEvent(AcademicEvents event) {
        if (event != null) {
            events.add(event);
            System.out.println("Event '" + event.getTitle() + "' added successfully.");
        } else {
            System.out.println("Cannot add a null event.");
        }
    }

    public static List<AcademicEvents> getAllEvents() {
        return Collections.unmodifiableList(events);
    }

    public static void listAllEvents() {
        if (events.isEmpty()) {
            System.out.println("No academic events registered yet.");
        }
        else {
            System.out.println("\n--- All Academic Events ---");
            for (AcademicEvents event : events) {
                event.displayDetails();
            }
            System.out.println("---------------------------\n");
        }
    }

}