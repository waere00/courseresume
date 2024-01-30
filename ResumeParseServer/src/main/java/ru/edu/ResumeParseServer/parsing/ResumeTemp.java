package ru.edu.ResumeParseServer.parsing;

/**
 * Класс используется для обертки POST сообщения от клиента и дальнейшей записи в БД
 */
public class ResumeTemp {
    public String city;
    public String title;
    public String skills;
    public String gender;
    public String url;
    public String id;
    public ResumeTemp(){}

    public ResumeTemp(String city, String title, String skills, String gender, String url) {
        this.city = city;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.url = url;
    }
    public ResumeTemp(String id,String city, String title, String skills, String gender, String url) {
        this.id = id;
        this.city = city;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.url = url;
    }
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url =url;
    }
}
