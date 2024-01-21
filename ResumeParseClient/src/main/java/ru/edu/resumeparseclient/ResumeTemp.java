package ru.edu.resumeparseclient;
/**
 * Вспомогательный класс для передачи данных в таблицу JavaFX
 */
public class ResumeTemp {
    public String id;
    public String areaName;
    public String title;
    public String skills;
    public String gender;
    public String alternateUrl;
    public ResumeTemp(){}

    public ResumeTemp(String areaName, String title, String skills, String gender, String alternateUrl) {
        this.areaName = areaName;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.alternateUrl = alternateUrl;
    }
    public ResumeTemp(String id, String areaName, String title, String skills, String gender, String alternateUrl) {
        this.id = id;
        this.areaName = areaName;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.alternateUrl = alternateUrl;
    }
}
