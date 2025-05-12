package Library.src;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class Members {
    private final int id;
    private String name;
    private String email;
    private static int userID = 0;
    private HashMap<Members, List<Books>> readingHistory;

    public Members(){
        this.id = userID++;
        this.readingHistory = new HashMap<>();
        this.readingHistory.put(this, new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<Members, List<Books>> getReadingHistory() {
        return readingHistory;
    }

    public void addBookToHistory(Books book) {
        this.readingHistory.get(this).add(book);
    }





}
