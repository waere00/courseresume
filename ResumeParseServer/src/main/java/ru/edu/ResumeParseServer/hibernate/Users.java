package ru.edu.ResumeParseServer.hibernate;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

/**
 * Класс сущности, взаимодействует с таблицей users
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "login")})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NaturalId
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private boolean admin;

    public Users(){}

    public Users(String login, String password, boolean admin) {
        this.login = login;
        this.password = password;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isAdmin() {
        return admin;
    };

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
