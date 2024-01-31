package ru.edu.resumeparseserver.model;

import jakarta.persistence.*;

/**
 * Класс сущности, взаимодействует с таблицей resumes
 */
@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "resumeId")
    private String resumeId;
    private int age;
    @Column(name = "areaId", length = 65535)
    private String areaId;

    @Column(name = "areaName", length = 65535)
    private String areaName;

    @Column(name = "site", length = 65535)
    private String site;

    @Column(name = "metro", length = 65535)
    private String metro;

    @Column(name = "ownerId", length = 65535)
    private String ownerId;

    @Column(name = "photo", length = 65535)
    private String photo;

    @Column(name = "title", length = 65535)
    private String title;

    @Column(name = "gender", length = 65535)
    private String gender;

    @Column(name = "salary", length = 65535)
    private String salary;

    @Column(name = "skills", length = 365535)
    private String skills;

    @Column(name = "actions", length = 65535)
    private String actions;

    @Column(name = "contact", length = 65535)
    private String contact;

    @Column(name = "download", length = 65535)
    private String download;

    @Column(name = "language", length = 65535)
    private String language;

    @Column(name = "schedule", length = 65535)
    private String schedule;

    @Column(name = "education", length = 265535)
    private String education;

    @Column(name ="favorited")
    private boolean favorited;

    @Column(name = "lastName", length = 65535)
    private String lastName;

    @Column(name = "portfolio", length = 365535)
    private String portfolio;

    @Column(name = "schedules", length = 65535)
    private String schedules;

    @Column(name = "skillSet", length = 65535)
    private String skillSet;

    @Column(name = "birthDate", length = 65535)
    private String birthDate;

    @Column(name = "createdAt", length = 65535)
    private String createdAt;

    @Column(name = "employment", length = 65535)
    private String employment;

    @Column(name = "experience", length = 65535)
    private String experience;

    @Column(name = "firstName", length = 65535)
    private String firstName;

    @Column(name = "relocation", length = 65535)
    private String relocation;

    @Column(name = "updatedAt", length = 65535)
    private String updatedAt;

    @Column(name = "certificate", length = 65535)
    private String certificate;

    @Column(name = "citizenship", length = 65535)
    private String citizenship;

    @Column(name = "employments", length = 65535)
    private String employments;

    @Column(name = "hasVehicle", length = 65535)
    private String hasVehicle;

    @Column(name = "middleName", length = 65535)
    private String middleName;

    @Column(name = "travelTime", length = 65535)
    private String travelTime;

    @Column(name = "workTicket", length = 65535)
    private String workTicket;

    @Column(name = "alternateUrl", length = 65535)
    private String alternateUrl;

    @Column(name = "hiddenFields", length = 65535)
    private String hiddenFields;

    @Column(name = "paidServices", length = 65535)
    private String paidServices;

    @Column(name = "resumeLocale", length = 65535)
    private String resumeLocale;

    @Column(name = "recommendation", length = 65535)
    private String recommendation;

    @Column(name = "specialization", length = 65535)
    private String specialization;

    @Column(name = "totalExperience", length = 65535)
    private String totalExperience;
    @Column(name ="canViewFullInfo")
    private boolean canViewFullInfo;

    @Column(name = "driverLicenseTypes", length = 65535)
    private String driverLicenseTypes;

    @Column(name = "negotiationsHistory", length = 65535)
    private String negotiationsHistory;

    @Column(name = "businessTripReadiness", length = 65535)
    private String businessTripReadiness;

    private float rating;

    public Resume() {}

    public Resume(String resumeId, int age, String areaId, String areaName, String site,
                  String metro, String ownerId, String photo, String title, String gender,
                  String salary, String skills, String actions, String contact,
                  String download, String language, String schedule, String education,
                  boolean favorited, String lastName, String portfolio, String schedules,
                  String skillSet, String birthDate, String createdAt, String employment,
                  String experience, String firstName, String relocation, String updatedAt,
                  String certificate, String citizenship, String employments, String hasVehicle,
                  String middleName, String travelTime, String workTicket, String alternateUrl,
                  String url, String hiddenFields, String paidServices, String resumeLocale,
                  String recommendation, String specialization, String totalExperience,
                  boolean canViewFullInfo, String driverLicenseTypes, String negotiationsHistory,
                  String businessTripReadiness, float rating) {

        this.resumeId = resumeId;
        this.age = age;
        this.areaId = areaId;
        this.areaName = areaName;
        this.site = site;
        this.metro = metro;
        this.ownerId = ownerId;
        this.photo = photo;
        this.title = title;
        this.gender = gender;
        this.salary = salary;
        this.skills = skills;
        this.actions = actions;
        this.contact = contact;
        this.download = download;
        this.language = language;
        this.schedule = schedule;
        this.education = education;
        this.favorited = favorited;
        this.lastName = lastName;
        this.portfolio = portfolio;
        this.schedules = schedules;
        this.skillSet = skillSet;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.employment = employment;
        this.experience = experience;
        this.firstName = firstName;
        this.relocation = relocation;
        this.updatedAt = updatedAt;
        this.certificate = certificate;
        this.citizenship = citizenship;
        this.employments = employments;
        this.hasVehicle = hasVehicle;
        this.middleName = middleName;
        this.travelTime = travelTime;
        this.workTicket = workTicket;
        this.alternateUrl = alternateUrl;
        this.hiddenFields = hiddenFields;
        this.paidServices = paidServices;
        this.resumeLocale = resumeLocale;
        this.recommendation = recommendation;
        this.specialization = specialization;
        this.totalExperience = totalExperience;
        this.canViewFullInfo = canViewFullInfo;
        this.driverLicenseTypes = driverLicenseTypes;
        this.negotiationsHistory = negotiationsHistory;
        this.businessTripReadiness = businessTripReadiness;
        this.rating = rating;
    }

    public Resume(String areaName, String title, String skills, String gender, String alternateUrl) {
        this.areaName = areaName;
        this.title = title;
        this.skills = skills;
        this.gender = gender;
        this.alternateUrl = alternateUrl;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAreaId() { return areaId; }
    public void setAreaId(String areaId) { this.areaId = areaId; }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getMetro() { return metro; }
    public void setMetro(String metro) { this.metro = metro; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getActions() { return actions; }
    public void setActions(String actions) { this.actions = actions; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getDownload() { return download; }
    public void setDownload(String download) { this.download = download; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public boolean isFavorited() { return favorited; }
    public void setFavorited(boolean favorited) { this.favorited = favorited; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }

    public String getSchedules() { return schedules; }
    public void setSchedules(String schedules) { this.schedules = schedules; }

    public String getSkillSet() { return skillSet; }
    public void setSkillSet(String skillSet) { this.skillSet = skillSet; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getEmployment() { return employment; }
    public void setEmployment(String employment) { this.employment = employment; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getRelocation() { return relocation; }
    public void setRelocation(String relocation) { this.relocation = relocation; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getCertificate() { return certificate; }
    public void setCertificate(String certificate) { this.certificate = certificate; }

    public String getCitizenship() { return citizenship; }
    public void setCitizenship(String citizenship) { this.citizenship = citizenship; }

    public String getEmployments() { return employments; }
    public void setEmployments(String employments) { this.employments = employments; }

    public String getHasVehicle() { return hasVehicle; }
    public void setHasVehicle(String hasVehicle) { this.hasVehicle = hasVehicle; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getTravelTime() { return travelTime; }
    public void setTravelTime(String travelTime) { this.travelTime = travelTime; }

    public String getWorkTicket() { return workTicket; }
    public void setWorkTicket(String workTicket) { this.workTicket = workTicket; }

    public String getAlternateUrl() { return alternateUrl; }
    public void setAlternateUrl(String alternateUrl) { this.alternateUrl = alternateUrl; }

    public String getHiddenFields() { return hiddenFields; }
    public void setHiddenFields(String hiddenFields) { this.hiddenFields = hiddenFields; }

    public String getPaidServices() { return paidServices; }
    public void setPaidServices(String paidServices) { this.paidServices = paidServices; }

    public String getResumeLocale() { return resumeLocale; }
    public void setResumeLocale(String resumeLocale) { this.resumeLocale = resumeLocale; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getTotalExperience() { return totalExperience; }
    public void setTotalExperience(String totalExperience) { this.totalExperience = totalExperience; }

    public boolean isCanViewFullInfo() { return canViewFullInfo; }
    public void setCanViewFullInfo(boolean canViewFullInfo) { this.canViewFullInfo = canViewFullInfo; }

    public String getDriverLicenseTypes() { return driverLicenseTypes; }
    public void setDriverLicenseTypes(String driverLicenseTypes) { this.driverLicenseTypes = driverLicenseTypes; }

    public String getNegotiationsHistory() { return negotiationsHistory; }
    public void setNegotiationsHistory(String negotiationsHistory) { this.negotiationsHistory = negotiationsHistory; }

    public String getBusinessTripReadiness() { return businessTripReadiness; }
    public void setBusinessTripReadiness(String businessTripReadiness) { this.businessTripReadiness = businessTripReadiness; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}
