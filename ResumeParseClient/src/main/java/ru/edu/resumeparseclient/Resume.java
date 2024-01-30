package ru.edu.resumeparseclient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Класс для взаимосвязи с таблицей JavaFX
 */
public class Resume {
    public StringProperty id;
    public StringProperty city;
    public StringProperty title;
    public StringProperty skills;
    public StringProperty gender;
    public StringProperty url;

    public Resume(String city, String title, String skills, String gender, String url) {
        this.city = new SimpleStringProperty(this, "city", city);
        this.title = new SimpleStringProperty(this, "title", title);
        this.skills = new SimpleStringProperty(this, "skills", skills);
        this.gender = new SimpleStringProperty(this, "gender", gender);
        this.url = new SimpleStringProperty(this, "url", url);
    }
    public Resume(String id, String city, String title, String skills, String gender, String url) {
        this.id = new SimpleStringProperty(this, "id", id);
        this.city = new SimpleStringProperty(this, "city", city);
        this.title = new SimpleStringProperty(this, "title", title);
        this.skills = new SimpleStringProperty(this, "skills", skills);
        this.gender = new SimpleStringProperty(this, "gender", gender);
        this.url = new SimpleStringProperty(this, "url", url);
    }

    public String getCity() {
        return city.get();
    }
    public String getId() {return id.get();}
    public StringProperty idProperty() {return id;}
    public void setId(String id) {this.id.set(id);}

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getSkills() {
        return skills.get();
    }

    public StringProperty skillsProperty() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills.set(skills);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }
}
