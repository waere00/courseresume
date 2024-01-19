package ru.edu.resumeparseclient;
/**
 * Вспомогательный класс для передачи данных в таблицу JavaFX
 */
public class ResumeTemp {
    public String id;
    public String city;
    public String title;
    public String skills;
    public String gender;
    public String url;
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
}
