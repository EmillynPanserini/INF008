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
    private final ObjectProperty<LocalDate> registrationDate;

    private User(Builder builder) {
        this.id = new SimpleStringProperty(builder.id);
        this.name = new SimpleStringProperty(builder.name);
        this.email = new SimpleStringProperty(builder.email);
        this.registrationDate = new SimpleObjectProperty<>(builder.registrationDate);
    }

    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> registrationDateProperty() { return registrationDate; }

    public String getId(){
        return id.get();
    }
    public String getName(){
        return name.get();
    }
    public String getEmail(){
        return email.get();
    }
    public LocalDate getRegistrationDate(){
        return registrationDate.get();
    }

    public void setId(String id){
        this.id.set(id);
    }
    public void setName(String name){
        this.name.set(name);
    }
    public void setEmail(String email){
        this.email.set(email);
    }
    public void setRegistrationDate(LocalDate registrationDate){
        this.registrationDate.set(registrationDate);
    }



    public static class Builder {
        private String id;
        private String name;
        private String email;
        private LocalDate registrationDate;


        public Builder(String id, String name, String email, LocalDate registrationDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.registrationDate = registrationDate;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withRegistrationDate(LocalDate registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public User build() {
            //  Adicionar validações antes de construir o objeto final
            return new User(this);
        }
    }
}