package scr.manager;

import scr.events.AcademicEvents;
import java.time.LocalDate;
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
    public static void reportEvent(String eventTypeFilter, LocalDate dateFilter) {
        List<AcademicEvents> filteredEvents = new ArrayList<>();

        String normalizedTypeFilter = (eventTypeFilter != null) ? eventTypeFilter.toLowerCase().trim() : null;

        for (AcademicEvents event : events) {
            boolean matchesType = true;
            boolean matchesDate = true;

            if (normalizedTypeFilter != null && !normalizedTypeFilter.isEmpty()) {

                String eventClassName = event.getClass().getSimpleName().toLowerCase();
                matchesType = eventClassName.contains(normalizedTypeFilter);
            }

            if (dateFilter != null) {
                matchesDate = event.getDate().equals(dateFilter);
            }

            if (matchesType && matchesDate) {
                filteredEvents.add(event);
            }
        }

        if (filteredEvents.isEmpty()) {
            System.out.println("\nNo events found matching the criteria.");
        } else {
            System.out.println("\n--- Academic Events Report ---");
            System.out.println("Criterion: Type = " + (eventTypeFilter != null && !eventTypeFilter.isEmpty() ? eventTypeFilter : "Any") +
                    ", Date = " + (dateFilter != null ? dateFilter : "Any"));
            for (AcademicEvents event : filteredEvents) {
                event.displayDetails();
            }
            System.out.println("------------------------------\n");
        }
    }
}

