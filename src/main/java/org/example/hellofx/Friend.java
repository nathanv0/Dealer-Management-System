package org.example.hellofx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Friend {
    private StringProperty name;
    private IntegerProperty age;

    // Get the property
    public StringProperty nameProperty() {return name; }
    public IntegerProperty ageProperty() {return age;}

    public Friend(String name, int age) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + getName() + '\'' +
                ", age=" + getAge() +
                '}';
    }
}


