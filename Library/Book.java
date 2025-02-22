package Library;

public class Book {
    private static int ISBN = 0;
    private int id = 0;
    private String title;
    private String author;
    private String publishYear;

    public Book(int id, String title, String author, String publishYear) {
        this.id = ISBN++;
        this.title = "";
        this.author = "";
        this.publishYear = "";
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getPublishYear() {
        return publishYear;
    }
    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }
    public int getId() {
        return id;
    }

}


