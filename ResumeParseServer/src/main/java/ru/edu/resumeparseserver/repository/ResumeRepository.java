package ru.edu.resumeparseserver.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.resumeparseserver.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE resume", nativeQuery = true)
    void truncateTable();
}