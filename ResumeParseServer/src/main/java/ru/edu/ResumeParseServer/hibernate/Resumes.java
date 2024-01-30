package ru.edu.ResumeParseServer.hibernate;

import jakarta.persistence.*;

/**
 * Класс сущности, взаимодействует с таблицей resumes
 */
@Entity
@Table(name = "resumes")
public class Resumes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "city", length = 65535, columnDefinition="TEXT")
    private String city;

    @Column(name = "title", length = 65535, columnDefinition="TEXT")
    private String title;

    @Column(name = "skills", length = 65535, columnDefinition="TEXT")
    private String skills;

    @Column(name = "gender", length = 65535, columnDefinition="TEXT")
    private String gender;

    @Column(name = "url", length = 65535, columnDefinition = "TEXT")
    private String url;

    public Resumes() {}
    public Resumes(String city, String title, String skills, String gender, String url) {
        this.city = city;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        this.url = url;
    }
}
