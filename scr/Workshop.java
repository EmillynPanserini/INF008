package scr;

public class Workshop extends AcademicEvents {
    @Override
    public void displayDetails() {
        System.out.println("--- Workshop Details ---");
        System.out.println("Title: " + getTitle());
        System.out.println("Date: " + getDate());
        System.out.println("Location: " + getLocation());
        System.out.println("Description: " + getDescription());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("------------------------");
    }
}
