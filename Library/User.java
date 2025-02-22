package Library;

import java.util.ArrayList;
import java.util.List;


public class User {
    private final int id;
    private String name;
    private static int userID = 0;
    private static final List<Loan> loanList= new ArrayList<>();

    public User(String name) {
        this.id = userID++;
        this.name = name;

    }
    public int getId() {
        return id;
    }
    public String getName() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Loan> getLoanList() {
        return loanList;
    }

    public void addLoan(Loan loan) {
        loanList.add(loan);
    }

    public void removeLoan(Loan loan) {
        loanList.remove(loan);
    }

    public int getLoanCount() {
        return loanList.size();
    }



}
