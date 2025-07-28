package br.edu.ifba.inf008.interfaces.models;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class User {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final ObjectProperty<LocalDate> registrationDate; // Usar ObjectProperty<LocalDate>

    public User(String id, String name, String email, LocalDate registrationDate) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.registrationDate = new SimpleObjectProperty<>(registrationDate);
    }

    // Getters for JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> registrationDateProperty() { return registrationDate; }

    // Getters for simple value
    public String getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public LocalDate getRegistrationDate() { return registrationDate.get(); }

    // Setters for simple value
    public void setId(String id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setEmail(String email) { this.email.set(email); }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate.set(registrationDate); }
}
