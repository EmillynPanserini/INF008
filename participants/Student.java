package participants;

public class Student extends Participant{
    private String registration;

    @Override
    public String toString() {
        return "Participant name: " + getName()+
                ", e-mail: " + getEmail()+
                ", registration" + registration;

    }

}
