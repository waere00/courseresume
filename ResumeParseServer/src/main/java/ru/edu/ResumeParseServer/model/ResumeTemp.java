package ru.edu.ResumeParseServer.model;

/**
 * Класс используется для обертки POST сообщения от клиента и дальнейшей записи в БД
 */
public class ResumeTemp {
    public String areaName;
    public String title;
    public String skills;
    public String gender;
    public String alternateUrl;
    public int id;
    public ResumeTemp(){}

    public ResumeTemp(String areaName, String title, String skills, String gender, String alternateUrl) {
        this.areaName = areaName;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.alternateUrl = alternateUrl;
    }
    public ResumeTemp(int id, String areaName, String title, String skills, String gender, String alternateUrl) {
        this.id = id;
        this.areaName = areaName;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.alternateUrl = alternateUrl;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getAlternateUrl() {
        return alternateUrl;
    }

    public void setAlternateUrl(String alternateUrl) {
        this.alternateUrl = alternateUrl;
    }
}
