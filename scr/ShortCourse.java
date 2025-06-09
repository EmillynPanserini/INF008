package scr;

public class ShortCourse extends AcademicEvents{
    private String instructor;

    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public void displayDetails() {
        System.out.println("Title: " + getTitle());
        System.out.println("Date: " + getDate());
        System.out.println("Location: " + getLocation());
        System.out.println("Description: " + getDescription());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Instructor: " + instructor);
        System.out.println("----------------------------");
    }
    @Override
    public String toString() {
        return super.toString() +
                ", Instructor: " + instructor;
    }
}