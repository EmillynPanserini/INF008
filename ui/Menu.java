package ui;

import manager.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    public void menuRegisterEvents() {
        Scanner sc = new Scanner(System.in);
        int choiceMenu = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("What do you want to do?\n");
            System.out.println("Register a:\n");
            System.out.println( "1.Lecture\n " +
                                "2. Workshop\n " +
                                "3. Academic Fair\n " +
                                "4. Short Courses\n  ");

            try {
                choiceMenu = sc.nextInt();
                if (choiceMenu >= 1 && choiceMenu <= 4) {
                    validInput = true; //
                } else {
                    System.out.println("Invalid option. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1, 2, 3, 4).");
                sc.next();
            }
        }
        switch (choiceMenu) {
            case 1:
                EventManager newLecture = new EventManager();
                newLecture.registerLecture();
                break;
            case 2:
                // Call method or logic to register a workshop
                break;
            case 3:
                // Call method or logic to register an academic fair
                break;
            case 4:
                // Call method or logic to register short courses
                break;
            default:
                System.out.println("An unexpected error occurred with your selection.");
        }
        sc.close();
    }
}