package scr;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;


abstract class AcademicEvents {
    private String titles;
    private String description;


    abstract String getEventTypeDetails();

}
