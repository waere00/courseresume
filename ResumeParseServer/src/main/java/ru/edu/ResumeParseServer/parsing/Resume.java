package ru.edu.ResumeParseServer.parsing;

/**
 * Класс хранит данные резюме соискателей
 */
public class Resume {
    private String city;
    private String title;
    private String skills;
    private String gender;
    private String alternative_url;

    public Resume(String city, String title, String skills, String gender, String alternative_url) {
        this.city = city;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.alternative_url = alternative_url;
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

    public String getAlternative_url() {
        return alternative_url;
    }

    public void setAlternative_url(String alternative_url) {
        this.alternative_url = alternative_url;
    }
}
