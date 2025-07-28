package br.edu.ifba.inf008.interfaces.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private final StringProperty isbn;
    private final StringProperty title;
    private final StringProperty name;
    private final StringProperty author;
    private final StringProperty publicationYear;
    private final StringProperty availableCopies;

    public Book(String isbn, String title, String name, String author, String publicationYear, String availableCopies) {
        this.isbn = new SimpleStringProperty(isbn);
        this.title = new SimpleStringProperty(title);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.publicationYear = new SimpleStringProperty(publicationYear);
        this.availableCopies = new SimpleStringProperty(availableCopies);
    }
    public String getIsbn() { return isbn.get();}
    public void setIsbn(String isbn) { this.isbn.set(isbn);}
    public String getTitle() { return title.get();}
    public void setTitle(String title) { this.title.set(title);}
    public String getName() { return name.get();}
    public void setName(String name) { this.name.set(name);}
    public String getAuthor() { return author.get();}
    public void setAuthor(String author) { this.author.set(author);}
    public String getPublicationYear() { return publicationYear.get();}
    public void setPublicationYear(String publicationYear) { this.publicationYear.set(publicationYear);}
    public String getAvailableCopies() { return availableCopies.get();}
    public void setAvailableCopies(String availableCopies) { this.availableCopies.set(availableCopies); }

}
