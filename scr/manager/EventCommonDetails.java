package scr.manager;


import java.time.LocalDate;

public record EventCommonDetails(
        String title,
        LocalDate date,
        String location,
        int capacity,
        String description
) {}