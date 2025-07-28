package br.edu.ifba.inf008.interfaces.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Loan {
    private final StringProperty id;
    private final ObjectProperty<User> user;
    private final ObjectProperty<Book> book;
    private final ObjectProperty<LocalDate> loanDate;
    private final ObjectProperty<LocalDate> returnDate;

    public Loan(String id, User user, Book book, LocalDate loanDate, LocalDate returnDate) {
        this.id = new SimpleStringProperty(id);
        this.user = new SimpleObjectProperty<>(user);
        this.book = new SimpleObjectProperty<>(book);
        this.loanDate = new SimpleObjectProperty<>(loanDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
    }

    // Getters for javaFX
    public StringProperty idProperty() { return id; }
    public ObjectProperty<User> userProperty() { return user; }
    public ObjectProperty<Book> bookProperty() { return book; }
    public ObjectProperty<LocalDate> loanDateProperty() { return loanDate; }
    public ObjectProperty<LocalDate> returnDateProperty() { return returnDate; }

    // Getters for simple value
    public String getId() { return id.get(); }
    public User getUser() { return user.get(); }
    public Book getBook() { return book.get(); }
    public LocalDate getLoanDate() { return loanDate.get(); }
    public LocalDate getReturnDate() { return returnDate.get(); }

    // Setters for simple value
    public void setId(String id) { this.id.set(id); }
    public void setUser(User user) { this.user.set(user); }
    public void setBook(Book book) { this.book.set(book); }
    public void setLoanDate(LocalDate loanDate) { this.loanDate.set(loanDate); }
    public void setReturnDate(LocalDate returnDate) { this.returnDate.set(returnDate); }
}
