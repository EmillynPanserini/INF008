package scr.participants;

public class Student extends Participant{
    private String registration;

    public void setRegistration(String studentRegister) {
    }

    @Override
    public String toString() {
        return "Participant name: " + getName()+
                ", e-mail: " + getEmail()+
                ", registration: " + registration;

    }
}
