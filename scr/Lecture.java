package scr;

public class Lecture extends AcademicEvents {
    private String speaker;


    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    @Override
    public void displayDetails() {
        System.out.println("--- Lecture Details ---");
        System.out.println("Title: " + getTitle());
        System.out.println("Date: " + getDate());
        System.out.println("Location: " + getLocation());
        System.out.println("Description: " + getDescription());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Speaker: " + speaker);
        System.out.println("-----------------------");
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Speaker: " + speaker;
    }
}
