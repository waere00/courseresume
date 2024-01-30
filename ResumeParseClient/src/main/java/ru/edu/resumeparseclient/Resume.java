package ru.edu.resumeparseclient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Класс для взаимосвязи с таблицей JavaFX
 */
public class Resume {
    public StringProperty id;
    public StringProperty areaName;
    public StringProperty title;
    public StringProperty skills;
    public StringProperty gender;
    public StringProperty alternateUrl;

    public Resume(String areaName, String title, String skills, String gender, String alternateUrl) {
        this.areaName = new SimpleStringProperty(this, "areaName", areaName);
        this.title = new SimpleStringProperty(this, "title", title);
        this.skills = new SimpleStringProperty(this, "skills", skills);
        this.gender = new SimpleStringProperty(this, "gender", gender);
        this.alternateUrl = new SimpleStringProperty(this, "alternateUrl", alternateUrl);
    }
    public Resume(String id, String areaName, String title, String skills, String gender, String alternateUrl) {
        this.id = new SimpleStringProperty(this, "id", id);
        this.areaName = new SimpleStringProperty(this, "areaName", areaName);
        this.title = new SimpleStringProperty(this, "title", title);
        this.skills = new SimpleStringProperty(this, "skills", skills);
        this.gender = new SimpleStringProperty(this, "gender", gender);
        this.alternateUrl = new SimpleStringProperty(this, "alternateUrl", alternateUrl);
    }

    public String getAreaName() {
        return areaName.get();
    }
    public String getId() {return id.get();}
    public StringProperty idProperty() {return id;}
    public void setId(String id) {this.id.set(id);}

    public StringProperty areaNameProperty() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName.set(areaName);
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

    public String getAlternateUrl() {
        return alternateUrl.get();
    }

    public StringProperty alternateUrlProperty() {
        return alternateUrl;
    }

    public void setAlternateUrl(String alternateUrl) {
        this.alternateUrl.set(alternateUrl);
    }
}
