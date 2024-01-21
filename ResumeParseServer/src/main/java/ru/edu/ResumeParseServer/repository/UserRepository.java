package ru.edu.ResumeParseServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.edu.ResumeParseServer.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByLogin(String login);
}
