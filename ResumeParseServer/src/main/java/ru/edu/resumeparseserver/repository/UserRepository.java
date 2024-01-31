package ru.edu.resumeparseserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.edu.resumeparseserver.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByLogin(String login);
}
