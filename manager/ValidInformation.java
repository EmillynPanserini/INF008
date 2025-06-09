package manager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ValidInformation {
    private static Scanner sc;

    public ValidInformation(){
        this.sc = new Scanner(System.in);
    }

    public static int readIntInput(String choice) {
        while (true) {
            System.out.print(choice);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }
    }

    public static String readStringInput(String choice) {
        System.out.print(choice);
        return sc.nextLine();
    }

}
