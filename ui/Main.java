package ui;

import scr.Lecture;

public class Main{
    public static void main (String[] args) {
        Lecture event1 = new Lecture();
        Menu menu = new Menu();

        menu.run();
        //event1.registerEvents();
    }
}
