package scr.manager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ValidInformation {
    private static Scanner sc = new Scanner(System.in);

    public static int readIntInput(String choice) {
        while (true) {
            System.out.print(choice);
            try {
                int input = sc.nextInt();
                sc.nextLine();
                return input;

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static String readStringInput(String choice) {
        System.out.print(choice);
        return sc.nextLine();
    }
}